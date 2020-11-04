package com.openpayd.conversion.conversion.service;

import com.openpayd.conversion.conversion.model.ConversionBo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IConversionService {

    ConversionBo convert(ConversionBo conversionRequest);

    List<ConversionBo> list(Long transactionId, LocalDate transactionDate, int page, int size);
}
