package com.openpayd.conversion.exchangerate.controller;

import com.openpayd.conversion.common.exception.RestTemplateException;
import com.openpayd.conversion.common.model.Response;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
public class ExchangeRateControllerImpl implements IExchangeRateController {

    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    @Override
    public ResponseEntity<Response> getRate(String sourceCurrency, String targetCurrency) {
        ExchangeRateBo bo = ExchangeRateBo.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .build();
        try {
            bo = exchangeRateService.getExchangeRate(bo);
            return Response.success().add("exchangeRate", bo).build();
        } catch (RestTemplateException e) {
            return Response.error(HttpStatus.INTERNAL_SERVER_ERROR).message("Error occurred while creating rest template.").build();
        } catch (RestClientException e) {
            return Response.error(HttpStatus.BAD_GATEWAY).message("Error occurred while retrieving exchange rates.").build();
        } catch (Exception e) {
            return Response.error(HttpStatus.INTERNAL_SERVER_ERROR).message("Error : " + e.getMessage()).build();
        }
    }
}
