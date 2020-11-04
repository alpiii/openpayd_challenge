package com.openpayd.conversion.conversion.controller;

import com.openpayd.conversion.common.model.Response;
import com.openpayd.conversion.conversion.model.ConversionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversion")
public interface IConversionController {

    @PostMapping("/convert")
    ResponseEntity<Response> convert(@Validated @RequestBody ConversionRequest request);

    @GetMapping("/list")
    ResponseEntity<Response> list(@RequestParam(required = false) String transactionId,
                                  @RequestParam(required = false) String transactionDate, @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int size);
}
