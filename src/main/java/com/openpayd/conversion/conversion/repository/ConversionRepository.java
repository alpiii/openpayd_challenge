package com.openpayd.conversion.conversion.repository;

import com.openpayd.conversion.conversion.entity.ConversionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConversionRepository extends CrudRepository<ConversionEntity, Long> {
}
