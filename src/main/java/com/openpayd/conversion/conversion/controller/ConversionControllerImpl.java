package com.openpayd.conversion.conversion.controller;

import com.openpayd.conversion.common.model.Response;
import com.openpayd.conversion.conversion.exception.ParameterException;
import com.openpayd.conversion.conversion.mapper.ConversionRequestMapper;
import com.openpayd.conversion.conversion.model.ConversionRequest;
import com.openpayd.conversion.conversion.service.ConversionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ConversionControllerImpl implements IConversionController {

    @Autowired
    ConversionServiceImpl conversionService;

    @Autowired
    ConversionRequestMapper conversionRequestMapper;

    @Override
    public ResponseEntity<Response> convert(ConversionRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Response> list(String transactionId, String transactionDate, int page, int size) {
        return null;
    }

    private void validateParameters(Long id, LocalDate date) {
        if (id == -1L && date == null) {
            throw new ParameterException("At least one of Transaction Id or Transaction Date should be entered.");
        }
    }

    private long validateTransactionId(String transactionId) throws ParameterException {
        if (transactionId == null || transactionId.isEmpty()) {
            return -1L;
        } else {
            try {
                return Long.valueOf(transactionId);
            } catch (Exception e) {
                throw new ParameterException("Invalid transaction id.");
            }
        }
    }

    private LocalDate validateTransactionDate(String transactionDate) throws ParameterException {
        if (transactionDate == null || transactionDate.isEmpty()) {
            return null;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(transactionDate, formatter);
            } catch (Exception e) {
                throw new ParameterException("Invalid date format.");
            }
        }
    }
}
