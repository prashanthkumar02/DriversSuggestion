package com.drivers.suggestion.service;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBaseService {
    ResponseEntity<List<NearestDrivers>> retrieveNearestDriversFrom(String storeID, int numOfDrivers) throws GenericRestExpception;
    ResponseEntity<String> insertStoreDetails(List<Store> storeDetails);
    ResponseEntity<String> updateStoreDetails(List<Store> storeDetails);
    ResponseEntity<String> publishDriverDetails(List<Driver> driverDetails);
    void insertDriverDetails(List<Driver> driverList);
}
