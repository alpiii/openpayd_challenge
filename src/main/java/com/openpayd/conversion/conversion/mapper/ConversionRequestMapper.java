package com.openpayd.conversion.conversion.mapper;

import com.openpayd.conversion.conversion.model.ConversionBo;
import com.openpayd.conversion.conversion.model.ConversionRequest;
import org.springframework.stereotype.Component;

@Component
public class ConversionRequestMapper {

    public ConversionBo convert(ConversionRequest request) {
        return ConversionBo.builder()
                .id(request.getId())
                .transactionDate(request.getTransactionDate())
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .sourceAmount(request.getSourceAmount())
                .exchangeRate(request.getExchangeRate())
                .build();
    }

    public ConversionRequest convert(ConversionBo bo) {
        return ConversionRequest.builder()
                .id(bo.getId())
                .transactionDate(bo.getTransactionDate())
                .sourceCurrency(bo.getSourceCurrency())
                .targetCurrency(bo.getTargetCurrency())
                .sourceAmount(bo.getSourceAmount())
                .exchangeRate(bo.getExchangeRate())
                .build();
    }
}
