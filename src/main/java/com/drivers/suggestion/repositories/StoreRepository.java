package com.drivers.suggestion.repositories;

import com.drivers.suggestion.model.Store;
import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {
}
