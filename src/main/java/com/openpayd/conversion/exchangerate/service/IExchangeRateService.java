package com.openpayd.conversion.exchangerate.service;

import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import org.springframework.stereotype.Service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public interface IExchangeRateService {

    ExchangeRateBo getExchangeRate(ExchangeRateBo exchangeRateRequest) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException;
}
