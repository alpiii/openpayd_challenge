package com.openpayd.conversion.conversion.service;

import com.openpayd.conversion.conversion.mapper.ConversionEntityMapper;
import com.openpayd.conversion.conversion.model.ConversionBo;
import com.openpayd.conversion.conversion.repository.ConversionRepository;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ConversionServiceImpl implements IConversionService {

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    ConversionEntityMapper mapper;

    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    @Override
    public ConversionBo convert(ConversionBo conversionRequest) {
        return null;
    }

    @Override
    public List<ConversionBo> list(Long transactionId, LocalDate transactionDate, int page, int size) {
        return null;
    }
}
