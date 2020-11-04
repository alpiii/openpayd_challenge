package com.openpayd.conversion.exchangerate.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
public class Rate {
    private String base;
    private HashMap<String, BigDecimal> rates;
    private String date;
}
