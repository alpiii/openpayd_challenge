package com.openpayd.conversion.conversion.service;

import com.openpayd.conversion.common.exception.RestTemplateException;
import com.openpayd.conversion.conversion.entity.ConversionEntity;
import com.openpayd.conversion.conversion.mapper.ConversionEntityMapper;
import com.openpayd.conversion.conversion.model.ConversionBo;
import com.openpayd.conversion.conversion.repository.ConversionRepository;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConversionServiceImpl implements IConversionService {

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    ConversionEntityMapper mapper;

    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    @Override
    public ConversionBo convert(ConversionBo conversionBo) throws RestTemplateException, RestClientException {
        ExchangeRateBo rate = ExchangeRateBo.builder()
                .sourceCurrency(conversionBo.getSourceCurrency())
                .targetCurrency(conversionBo.getTargetCurrency())
                .build();
        rate = exchangeRateService.getExchangeRate(rate);
        conversionBo.setTransactionDate(LocalDate.now());
        conversionBo.setExchangeRate(rate.getExchangeRate());
        conversionBo.setTargetAmount(conversionBo.getSourceAmount().multiply(conversionBo.getExchangeRate()));
        ConversionEntity entity = mapper.convert(conversionBo);
        entity = conversionRepository.save(entity);
        ConversionBo bo = mapper.convert(entity);
        return bo;
    }

    @Override
    public List<ConversionBo> list(Long transactionId, LocalDate transactionDate, int page, int size) {
        if (transactionId == -1L) {
            Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
            Page<ConversionEntity> entityList = conversionRepository.findAllByTransactionDate(transactionDate, pageRequest);
            List<ConversionBo> conversionList = new ArrayList<>();
            entityList.getContent().forEach(e->conversionList.add(mapper.convert(e)));
            return conversionList;
        } else {
            Optional<ConversionEntity> conversionEntity = null;
            if (transactionDate != null) {
                conversionEntity = conversionRepository.findByIdAndTransactionDate(transactionId, transactionDate);
            } else {
                conversionEntity = conversionRepository.findById(transactionId);
            }
            List<ConversionBo> conversionList = new ArrayList<>();
            if (conversionEntity != null && conversionEntity.isPresent()) {
                conversionList.add(mapper.convert(conversionEntity.get()));
            }
            return conversionList;
        }
    }
}
