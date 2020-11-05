package com.openpayd.conversion.conversion.service;

import com.openpayd.conversion.conversion.entity.ConversionEntity;
import com.openpayd.conversion.conversion.mapper.ConversionEntityMapper;
import com.openpayd.conversion.conversion.model.ConversionBo;
import com.openpayd.conversion.conversion.repository.ConversionRepository;
import com.openpayd.conversion.exchangerate.model.ExchangeRateBo;
import com.openpayd.conversion.exchangerate.service.ExchangeRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ConversionServiceTest {

    @Mock
    private ConversionRepository conversionRepository;

    @Mock
    private ConversionEntityMapper conversionEntityMapper;

    @Mock
    private ExchangeRateServiceImpl exchangeRateService;

    @InjectMocks
    private ConversionServiceImpl conversionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private ConversionEntity createConversionEntity(Long id, LocalDate transactionDate, String sourceCurrency, String targetCurrency,
                                                    BigDecimal sourceAmount, BigDecimal targetAmount, BigDecimal exchangeRate) {
        ConversionEntity entity = new ConversionEntity();
        entity.setId(id);
        entity.setTransactionDate(transactionDate);
        entity.setSourceCurrency(sourceCurrency);
        entity.setTargetCurrency(targetCurrency);
        entity.setSourceAmount(sourceAmount);
        entity.setTargetAmount(targetAmount);
        entity.setExchangeRate(exchangeRate);
        return entity;
    }

    private ConversionBo createConversionBo(Long id, LocalDate transactionDate, String sourceCurrency, String targetCurrency,
                                            BigDecimal sourceAmount, BigDecimal targetAmount, BigDecimal exchangeRate) {
        ConversionBo bo = ConversionBo.builder()
                .id(id)
                .transactionDate(transactionDate)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .sourceAmount(sourceAmount)
                .targetAmount(targetAmount)
                .exchangeRate(exchangeRate)
                .build();
        return bo;
    }

    private List<ConversionEntity> createConversionEntityList() {
        List<ConversionEntity> entityList = new ArrayList<>();
        entityList.add(createConversionEntity(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370)));
        entityList.add(createConversionEntity(2L, LocalDate.now(), "USD", "EUR",
                new BigDecimal(977.5), new BigDecimal(850), new BigDecimal(1.15)));
        return entityList;
    }

    private List<ConversionBo> createConversionBoList() {
        List<ConversionBo> boList = new ArrayList<>();
        boList.add(createConversionBo(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370)));
        boList.add(createConversionBo(2L, LocalDate.now(), "USD", "EUR",
                new BigDecimal(977.5), new BigDecimal(850), new BigDecimal(1.15)));
        return boList;
    }

    private ExchangeRateBo createExchangeRateBo(String sourceCurrency, String targetCurrency, BigDecimal rate) {
        ExchangeRateBo bo = ExchangeRateBo.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .build();
        if (rate != null) {
            bo.setExchangeRate(rate);
        }
        return bo;
    }

    @Test
    void getConversionByIdTest() {
        ConversionEntity entity = createConversionEntity(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370));
        ConversionBo bo = createConversionBo(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370));

        when(conversionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(conversionEntityMapper.convert(any(ConversionEntity.class))).thenReturn(bo);

        List<ConversionBo> boList = conversionService.list(1L, null, 0, 1);

        assertEquals(1L, boList.get(0).getId());
        assertEquals(new BigDecimal(8370), boList.get(0).getTargetAmount());
    }

    @Test
    void getConversionByTransactionDateTest() {
        List<ConversionEntity> entityList = createConversionEntityList();
        Page<ConversionEntity> entities = new PageImpl<ConversionEntity>(entityList);
        List<ConversionBo> boList = createConversionBoList();

        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id"));

        when(conversionRepository.findAllByTransactionDate(LocalDate.now(), pageRequest)).thenReturn(entities);
        when(conversionEntityMapper.convert(entityList.get(0))).thenReturn(boList.get(0));
        when(conversionEntityMapper.convert(entityList.get(1))).thenReturn(boList.get(1));

        List<ConversionBo> boResultList = conversionService.list(-1L, LocalDate.now(), 0, 3);

        assertEquals(2, boResultList.size());
        assertEquals(1L, boResultList.get(0).getId());
        assertEquals(new BigDecimal(8370), boResultList.get(0).getTargetAmount());
        assertEquals(2L, boResultList.get(1).getId());
        assertEquals(new BigDecimal(977.5), boResultList.get(1).getSourceAmount());
        assertEquals(new BigDecimal(1.15), boResultList.get(1).getExchangeRate());
    }

    @Test
    void getConversionByIdAndTransactionDateTest() {
        List<ConversionEntity> entityList = createConversionEntityList();
        Page<ConversionEntity> entities = new PageImpl<ConversionEntity>(entityList);
        List<ConversionBo> boList = createConversionBoList();

        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id"));

        when(conversionRepository.findAllByTransactionDate(LocalDate.now().minusDays(1), pageRequest)).thenReturn(entities);
        when(conversionEntityMapper.convert(entityList.get(0))).thenReturn(boList.get(0));
        when(conversionEntityMapper.convert(entityList.get(1))).thenReturn(boList.get(1));

        List<ConversionBo> boResultList = conversionService.list(1L, LocalDate.now().minusDays(1), 0, 3);

        assertEquals(0, boResultList.size());
    }

    @Test
    void getConversionByTransactionDatePagingTest() {
        List<ConversionEntity> entityList = createConversionEntityList();
        entityList.remove(entityList.size() - 1);
        Page<ConversionEntity> entities = new PageImpl<ConversionEntity>(entityList);
        List<ConversionBo> boList = createConversionBoList();

        Pageable pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));

        when(conversionRepository.findAllByTransactionDate(LocalDate.now(), pageRequest)).thenReturn(entities);
        when(conversionEntityMapper.convert(entityList.get(0))).thenReturn(boList.get(0));

        List<ConversionBo> boResultList = conversionService.list(-1L, LocalDate.now(), 0, 1);

        assertEquals(1, boResultList.size());
    }

    @Test
    void convertTest() {
        ConversionBo conversionRequestBo = createConversionBo(null, null, "USD", "TRY",
                new BigDecimal(1000), null, null);
        ConversionBo conversionResultBo = createConversionBo(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370));
        ConversionEntity entity = createConversionEntity(1L, LocalDate.now(), "USD", "TRY",
                new BigDecimal(1000), new BigDecimal(8370), new BigDecimal(8.370));

        ExchangeRateBo exchangeRateBoResponse = createExchangeRateBo("USD", "TRY", new BigDecimal(8.370));
        when(exchangeRateService.getExchangeRate(any(ExchangeRateBo.class))).thenReturn(exchangeRateBoResponse);
        when(conversionEntityMapper.convert(any(ConversionEntity.class))).thenReturn(conversionResultBo);
        when(conversionEntityMapper.convert(any(ConversionBo.class))).thenReturn(entity);
        when(conversionRepository.save(any(ConversionEntity.class))).thenReturn(entity);

        ConversionBo result = conversionService.convert(conversionRequestBo);
        assertEquals(conversionResultBo.getTargetAmount(), result.getTargetAmount());
        assertEquals(conversionResultBo.getExchangeRate(), result.getExchangeRate());
    }
}
