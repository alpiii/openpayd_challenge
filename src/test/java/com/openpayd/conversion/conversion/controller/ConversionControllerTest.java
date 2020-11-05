package com.openpayd.conversion.conversion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openpayd.conversion.conversion.mapper.ConversionRequestMapper;
import com.openpayd.conversion.conversion.model.ConversionBo;
import com.openpayd.conversion.conversion.model.ConversionRequest;
import com.openpayd.conversion.conversion.service.ConversionServiceImpl;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ConversionControllerImpl.class)
@AutoConfigureRestDocs
public class ConversionControllerTest {

    @MockBean
    ConversionServiceImpl conversionService;

    @MockBean
    ConversionRequestMapper conversionRequestMapper;

    @Autowired
    private MockMvc mockMvc;

    static ResultMatcher jsonResult = content().contentType(MediaType.APPLICATION_JSON);

    private ConversionBo createConversionBo(Long id, LocalDate transactionDate, String sourceCurrency, String targetCurrency,
                                            BigDecimal sourceAmount, BigDecimal targetAmount, BigDecimal exchangeRate) {
        ConversionBo bo = ConversionBo.builder()
                .id(id)
                .transactionDate(transactionDate)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .sourceAmount(sourceAmount)
                .targetAmount(targetAmount)
                .exchangeRate(exchangeRate)
                .build();
        return bo;
    }

    private List<ConversionBo> createConversionBoList() {
        List<ConversionBo> boList = new ArrayList<>();
        boList.add(createConversionBo(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370)));
        boList.add(createConversionBo(2L, LocalDate.now(), "USD", "EUR",
                new BigDecimal(977.5), new BigDecimal(850), new BigDecimal(1.15)));
        return boList;
    }

    @Test
    void convertSuccessTest() throws Exception {
        ConversionRequest request = ConversionRequest.builder()
                .sourceCurrency("USD")
                .targetCurrency("TRY")
                .sourceAmount(new BigDecimal(1000))
                .build();
        ConversionBo boRequest = ConversionBo.builder()
                .sourceCurrency("USD")
                .targetCurrency("TRY")
                .sourceAmount(new BigDecimal(1000))
                .build();
        ConversionBo boResponse = ConversionBo.builder()
                .id(1L)
                .transactionDate(LocalDate.now())
                .sourceCurrency("USD")
                .targetCurrency("TRY")
                .sourceAmount(new BigDecimal(1000))
                .targetAmount(new BigDecimal(8370))
                .exchangeRate(new BigDecimal(8.370))
                .build();

        when(conversionRequestMapper.convert(request)).thenReturn(boRequest);
        when(conversionService.convert(boRequest)).thenReturn(boResponse);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        mockMvc.perform(post("/api/conversion/convert").content(requestJson).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.conversion.id").value(1L))
                .andExpect(jsonPath("$.data.conversion.transactionDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.conversion.sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.conversion.targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.data.conversion.sourceAmount").value(1000))
                .andExpect(jsonPath("$.data.conversion.targetAmount").value(8370))
                .andExpect(jsonPath("$.data.conversion.exchangeRate").value(8.370))
                .andDo(document("conversion/convert", responseFields(
                        fieldWithPath("data.conversion.id").description("id"),
                        fieldWithPath("data.conversion.transactionDate").description("Transaction Date"),
                        fieldWithPath("data.conversion.sourceCurrency").description("Source Currency"),
                        fieldWithPath("data.conversion.targetCurrency").description("Target Currency"),
                        fieldWithPath("data.conversion.sourceAmount").description("Source Amount"),
                        fieldWithPath("data.conversion.targetAmount").description("Target Amount"),
                        fieldWithPath("data.conversion.exchangeRate").description("Exchange Rate"))));
    }

    @Test
    void listWithIdTest() throws Exception {
        Long id = 1L;
        int page = 0; // default value
        int size = 10; // default value
        ConversionBo responseBo = createConversionBo(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370));
        List<ConversionBo> boList = new ArrayList<>();
        boList.add(responseBo);

        when(conversionService.list(id, null, page, size)).thenReturn(boList);

        mockMvc.perform(get("/api/conversion/list?transactionId=1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.conversions[0].id").value(1L))
                .andExpect(jsonPath("$.data.conversions[0].transactionDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.conversions[0].sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.conversions[0].targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.data.conversions[0].sourceAmount").value(1000))
                .andExpect(jsonPath("$.data.conversions[0].targetAmount").value(8370))
                .andExpect(jsonPath("$.data.conversions[0].exchangeRate").value(8.370))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.conversions[0].id").description("id"),
                        fieldWithPath("data.conversions[0].transactionDate").description("Transaction Date"),
                        fieldWithPath("data.conversions[0].sourceCurrency").description("Source Currency"),
                        fieldWithPath("data.conversions[0].targetCurrency").description("Target Currency"),
                        fieldWithPath("data.conversions[0].sourceAmount").description("Source Amount"),
                        fieldWithPath("data.conversions[0].targetAmount").description("Target Amount"),
                        fieldWithPath("data.conversions[0].exchangeRate").description("Exchange Rate"))));
    }

    @Test
    void listWithTransactionDateWithPageTest() throws Exception {
        int page = 0; // default value
        int size = 1; // default value
        LocalDate date = LocalDate.now();
        List<ConversionBo> boList = createConversionBoList();
        boList.remove(boList.size() - 1);

        when(conversionService.list(-1L, date, page, size)).thenReturn(boList);

        mockMvc.perform(get("/api/conversion/list?page=0&size=1&transactionDate=" + date.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.conversions[0].id").value(1L))
                .andExpect(jsonPath("$.data.conversions[0].transactionDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.conversions[0].sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.conversions[0].targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.data.conversions[0].sourceAmount").value(1000))
                .andExpect(jsonPath("$.data.conversions[0].targetAmount").value(8370))
                .andExpect(jsonPath("$.data.conversions[0].exchangeRate").value(8.370))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.conversions[0].id").description("id"),
                        fieldWithPath("data.conversions[0].transactionDate").description("Transaction Date"),
                        fieldWithPath("data.conversions[0].sourceCurrency").description("Source Currency"),
                        fieldWithPath("data.conversions[0].targetCurrency").description("Target Currency"),
                        fieldWithPath("data.conversions[0].sourceAmount").description("Source Amount"),
                        fieldWithPath("data.conversions[0].targetAmount").description("Target Amount"),
                        fieldWithPath("data.conversions[0].exchangeRate").description("Exchange Rate"))));
    }

    @Test
    void listWithTransactionDateWithoutPageTest() throws Exception {
        int page = 0; // default value
        int size = 10; // default value
        LocalDate date = LocalDate.now();
        List<ConversionBo> boList = createConversionBoList();

        when(conversionService.list(-1L, date, page, size)).thenReturn(boList);

        mockMvc.perform(get("/api/conversion/list?transactionDate=" + date.toString()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.conversions[0].id").value(1L))
                .andExpect(jsonPath("$.data.conversions[0].transactionDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.conversions[0].sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.conversions[0].targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.data.conversions[0].sourceAmount").value(1000))
                .andExpect(jsonPath("$.data.conversions[0].targetAmount").value(8370))
                .andExpect(jsonPath("$.data.conversions[0].exchangeRate").value(8.370))
                .andExpect(jsonPath("$.data.conversions[1].id").value(2L))
                .andExpect(jsonPath("$.data.conversions[1].transactionDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.conversions[1].sourceCurrency").value("USD"))
                .andExpect(jsonPath("$.data.conversions[1].targetCurrency").value("EUR"))
                .andExpect(jsonPath("$.data.conversions[1].sourceAmount").value(977.5))
                .andExpect(jsonPath("$.data.conversions[1].targetAmount").value(850))
                .andExpect(jsonPath("$.data.conversions[1].exchangeRate").value(1.15))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.conversions[0].id").description("id"),
                        fieldWithPath("data.conversions[0].transactionDate").description("Transaction Date"),
                        fieldWithPath("data.conversions[0].sourceCurrency").description("Source Currency"),
                        fieldWithPath("data.conversions[0].targetCurrency").description("Target Currency"),
                        fieldWithPath("data.conversions[0].sourceAmount").description("Source Amount"),
                        fieldWithPath("data.conversions[0].targetAmount").description("Target Amount"),
                        fieldWithPath("data.conversions[0].exchangeRate").description("Exchange Rate"),
                        fieldWithPath("data.conversions[1].id").description("id"),
                        fieldWithPath("data.conversions[1].transactionDate").description("Transaction Date"),
                        fieldWithPath("data.conversions[1].sourceCurrency").description("Source Currency"),
                        fieldWithPath("data.conversions[1].targetCurrency").description("Target Currency"),
                        fieldWithPath("data.conversions[1].sourceAmount").description("Source Amount"),
                        fieldWithPath("data.conversions[1].targetAmount").description("Target Amount"),
                        fieldWithPath("data.conversions[1].exchangeRate").description("Exchange Rate"))));
    }

    @Test
    void listInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/conversion/list?transactionId=a").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.message").value("Invalid transaction id."))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.error").description("error"),
                        fieldWithPath("data.message").description("message"))));
    }

    @Test
    void listInvalidTransactionDateTest() throws Exception {
        mockMvc.perform(get("/api/conversion/list?transactionDate=a").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.message").value("Invalid date format."))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.error").description("error"),
                        fieldWithPath("data.message").description("message"))));
    }

    @Test
    void listNoParameterTest() throws Exception {
        mockMvc.perform(get("/api/conversion/list").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.message").value("At least one of Transaction Id or Transaction Date should be entered."))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.error").description("error"),
                        fieldWithPath("data.message").description("message"))));
    }

    @Test
    void listEmptyTest() throws Exception {
        mockMvc.perform(get("/api/conversion/list?transactionId=-2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonResult)
                .andExpect(jsonPath("$.data.conversions").value(IsEmptyCollection.empty()))
                .andDo(document("conversion/list", responseFields(
                        fieldWithPath("data.conversions").description("conversion list"))));
    }
}
