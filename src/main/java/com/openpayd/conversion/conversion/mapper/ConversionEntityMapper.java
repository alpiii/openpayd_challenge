package com.openpayd.conversion.conversion.mapper;

import com.openpayd.conversion.conversion.entity.ConversionEntity;
import com.openpayd.conversion.conversion.model.ConversionBo;
import org.springframework.stereotype.Component;

@Component
public class ConversionEntityMapper {

    public ConversionEntity convert(ConversionBo bo) {
        ConversionEntity entity = new ConversionEntity();
        entity.setTransactionDate(bo.getTransactionDate());
        entity.setSourceCurrency(bo.getSourceCurrency());
        entity.setTargetCurrency(bo.getTargetCurrency());
        entity.setSourceAmount(bo.getSourceAmount());
        entity.setTargetAmount(bo.getTargetAmount());
        entity.setExchangeRate(bo.getExchangeRate());
        return entity;
    }

    public ConversionBo convert(ConversionEntity entity) {
        return ConversionBo.builder()
                .id(entity.getId())
                .transactionDate(entity.getTransactionDate())
                .sourceCurrency(entity.getSourceCurrency())
                .targetCurrency(entity.getTargetCurrency())
                .sourceAmount(entity.getSourceAmount())
                .targetAmount(entity.getTargetAmount())
                .exchangeRate(entity.getExchangeRate())
                .build();
    }
}
