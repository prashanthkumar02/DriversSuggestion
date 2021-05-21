package com.drivers.suggestion.repositories;

import com.drivers.suggestion.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    Store findByStoreID(String storeID);
}
