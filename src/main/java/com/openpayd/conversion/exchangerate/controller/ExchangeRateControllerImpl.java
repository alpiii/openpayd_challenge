package com.openpayd.conversion.exchangerate.controller;

import com.openpayd.conversion.common.model.Response;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class ExchangeRateControllerImpl implements IExchangeRateController {

    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    @Override
    public ResponseEntity<Response> convert(String sourceCurrency, String targetCurrency) {
        return null;
    }
}
