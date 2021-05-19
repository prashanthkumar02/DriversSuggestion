package com.drivers.suggestion.repositories;

import com.drivers.suggestion.model.Driver;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, Integer> {
}
