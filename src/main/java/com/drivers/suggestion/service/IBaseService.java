package com.drivers.suggestion.service;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.NoDataFoundException;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBaseService {
    ResponseEntity<List<NearestDrivers>> retrieveNearestDriversFrom(String storeID, int numOfDrivers) throws NoDataFoundException;
    ResponseEntity<String> insertStoreDetails(List<Store> storeDetails);
    ResponseEntity<String> insertDriverDetails(List<Driver> driverDetails);
}
