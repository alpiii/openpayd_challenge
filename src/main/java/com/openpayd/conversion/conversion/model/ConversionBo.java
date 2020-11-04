package com.openpayd.conversion.conversion.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ConversionBo {

    @NotNull
    private Long id;

    @NotNull private LocalDate transactionDate;

    @NotNull private String sourceCurrency;

    @NotNull private String targetCurrency;

    @NotNull private BigDecimal sourceAmount;

    @NotNull private BigDecimal targetAmount;

    @NotNull private BigDecimal exchangeRate;
}
