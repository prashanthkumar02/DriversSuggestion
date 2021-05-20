package com.drivers.suggestion.repositories;

import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverRepository extends CrudRepository<Driver, Integer> {

    @Query(name = "nearestDriversMapping", nativeQuery = true)
    List<NearestDrivers> getNearestDrivers(String storeID, int numOfDrivers);
}
