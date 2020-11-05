package com.openpayd.conversion.exchangerate.service;

import com.openpayd.conversion.common.exception.RestTemplateException;
import com.openpayd.conversion.common.restconfig.RestTemplateConfig;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.model.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateServiceImpl implements IExchangeRateService {

    @Autowired
    RestTemplateConfig restTemplateConfig;

    @Override
    public ExchangeRateBo getExchangeRate(ExchangeRateBo exchangeRateBo) throws RestTemplateException,
            RestClientException {

        final String uri = "https://api.ratesapi.io/api/latest?base=" + exchangeRateBo.getSourceCurrency() + "&symbols=" + exchangeRateBo.getTargetCurrency();

        RestTemplate restTemplate = restTemplateConfig.createRestTemplate();
        Rate result = restTemplate.getForObject(uri, Rate.class);
        exchangeRateBo.setExchangeRate(result.getRates().get(exchangeRateBo.getTargetCurrency()));
        return exchangeRateBo;
    }
}
