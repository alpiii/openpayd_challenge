package com.openpayd.conversion.exchangerate.controller;

import com.openpayd.conversion.common.exception.RestTemplateException;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExchangeRateControllerImpl.class)
@AutoConfigureRestDocs
public class ExchangeRateControllerTest {

    @MockBean
    ExchangeRateServiceImpl exchangeRateService;

    @Autowired
    private MockMvc mockMvc;

    static ResultMatcher jsonResult = content().contentType(MediaType.APPLICATION_JSON);

    @Test
    void convertSuccessTest() throws Exception {
        ExchangeRateBo rateRequestBo = ExchangeRateBo.builder().sourceCurrency("USD").targetCurrency("TRY").build();
        ExchangeRateBo rateResultBo = ExchangeRateBo.builder()
                .sourceCurrency("USD")
                .targetCurrency("TRY")
                .exchangeRate(new BigDecimal(8.370))
                .build();

        when(exchangeRateService.getExchangeRate(rateRequestBo)).thenReturn(rateResultBo);

        mockMvc.perform(
                get("/api/exchangerate/getrate/USD/TRY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.exchangeRate.sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.exchangeRate.targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.data.exchangeRate.exchangeRate").value(8.370))
                .andDo(document("exchangerate/getrate",
                        responseFields(fieldWithPath("data.exchangeRate.sourceCurrency").description("Source currency"),
                                fieldWithPath("data.exchangeRate.targetCurrency").description("Target Currency"),
                                fieldWithPath("data.exchangeRate.exchangeRate").description("Exchange Rate"))));
    }

    @Test void convertRestTemplateExceptionTest() throws Exception {
        ExchangeRateBo rateRequestBo = ExchangeRateBo.builder().sourceCurrency("USD").targetCurrency("TRY").build();

        when(exchangeRateService.getExchangeRate(rateRequestBo)).thenThrow(new RestTemplateException());

        mockMvc.perform(
                get("/api/exchangerate/getrate/USD/TRY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.data.message").value("Error occurred while creating rest template."))
                .andDo(document("exchangerate/getrate",
                        responseFields(fieldWithPath("data.error").description("error"), fieldWithPath("data.message").description("message"))));
    }

    @Test void convertRestClientExceptionTest() throws Exception {
        ExchangeRateBo rateRequestBo = ExchangeRateBo.builder().sourceCurrency("USD").targetCurrency("TRY").build();

        when(exchangeRateService.getExchangeRate(rateRequestBo)).thenThrow(new RestClientException(""));

        mockMvc.perform(
                get("/api/exchangerate/getrate/USD/TRY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("BAD_GATEWAY"))
                .andExpect(jsonPath("$.data.message").value("Error occurred while retrieving exchange rates."))
                .andDo(document("exchangerate/getrate",
                        responseFields(fieldWithPath("data.error").description("error"), fieldWithPath("data.message").description("message"))));
    }

    @Test void convertOtherExceptionTest() throws Exception {
        ExchangeRateBo rateRequestBo = ExchangeRateBo.builder().sourceCurrency("USD").targetCurrency("TRY").build();

        when(exchangeRateService.getExchangeRate(rateRequestBo)).thenThrow(new NullPointerException());

        mockMvc.perform(
                get("/api/exchangerate/getrate/USD/TRY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.data.message").value("Error : null"))
                .andDo(document("exchangerate/getrate",
                        responseFields(fieldWithPath("data.error").description("error"), fieldWithPath("data.message").description("message"))));
    }
}
