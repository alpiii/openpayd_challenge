package com.openpayd.conversion.exchangerate.model;

import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRateBo {

    @NotNull private String sourceCurrency;

    @NotNull private String targetCurrency;

    private BigDecimal exchangeRate;
}
