package com.openpayd.conversion.conversion.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ConversionRequest {

    private Long id;

    private LocalDate transactionDate;

    @NotNull
    private String sourceCurrency;

    @NotNull private String targetCurrency;

    @NotNull private BigDecimal sourceAmount;

    private BigDecimal exchangeRate;
}
