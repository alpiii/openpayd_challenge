package com.openpayd.conversion.conversion.repository;

import com.openpayd.conversion.conversion.entity.ConversionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ConversionRepository extends CrudRepository<ConversionEntity, Long> {

    Optional<ConversionEntity> findByIdAndTransactionDate(Long id, LocalDate transactionDate);

    Page<ConversionEntity> findAllByTransactionDate(LocalDate transactionDate, Pageable pageRequest);
}
