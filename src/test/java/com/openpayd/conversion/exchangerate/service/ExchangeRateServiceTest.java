package com.openpayd.conversion.exchangerate.service;

import com.openpayd.conversion.common.restconfig.RestTemplateConfig;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.model.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceTest {

    @Mock
    RestTemplateConfig restTemplateConfig;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ExchangeRateServiceImpl exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private ExchangeRateBo createExchangeRateBo(String sourceCurrency, String targetCurrency, BigDecimal rate) {
        ExchangeRateBo bo = ExchangeRateBo.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .build();
        if (rate != null) {
            bo.setExchangeRate(rate);
        }
        return bo;
    }

    @Test
    void getExchangeRateTest() {
        ExchangeRateBo requestBo = createExchangeRateBo("USD", "TRY", null);
        Rate rate = new Rate();
        rate.setBase("USD");
        rate.setDate(LocalDate.now().toString());
        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put("TRY", new BigDecimal(8.370));
        rate.setRates(rates);

        when(restTemplateConfig.createRestTemplate()).thenReturn(restTemplate);
        when(restTemplate.getForObject("https://api.ratesapi.io/api/latest?base=USD&symbols=TRY", Rate.class)).thenReturn(rate);

        ExchangeRateBo result = exchangeRateService.getExchangeRate(requestBo);

        assertEquals(new BigDecimal(8.370), result.getExchangeRate());
    }
}
