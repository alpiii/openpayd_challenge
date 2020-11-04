package com.openpayd.conversion.exchangerate.service;

import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import org.springframework.stereotype.Service;

@Service
public interface IExchangeRateService {

    ExchangeRateBo getExchangeRate(ExchangeRateBo exchangeRateRequest);
}
