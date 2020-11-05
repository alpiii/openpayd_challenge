package com.openpayd.conversion.exchangerate.controller;

import com.openpayd.conversion.common.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchangerate")
public interface IExchangeRateController {

    @GetMapping("/getrate/{sourceCurrency}/{targetCurrency}")
    ResponseEntity<Response> getRate(@PathVariable("sourceCurrency") String sourceCurrency,
                                     @PathVariable("targetCurrency") String targetCurrency);
}
