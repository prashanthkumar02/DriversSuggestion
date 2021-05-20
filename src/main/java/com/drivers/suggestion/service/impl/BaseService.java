package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.NoDataFoundException;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.repositories.DriverRepository;
import com.drivers.suggestion.repositories.StoreRepository;
import com.drivers.suggestion.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService implements IBaseService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    DriverRepository driverRepository;

    @Override
    public ResponseEntity<List<NearestDrivers>> retrieveNearestDriversFrom(String storeID, int numOfDrivers) throws NoDataFoundException {
        ResponseEntity<List<NearestDrivers>> responseEntity;
        List<NearestDrivers> closestDrivers = driverRepository.getNearestDrivers(storeID, numOfDrivers);

        if(!closestDrivers.isEmpty()) {
            responseEntity = new ResponseEntity<>(closestDrivers, HttpStatus.OK);
            return responseEntity;
        } else {
            throw new NoDataFoundException("No data found for storeId - " + storeID);
        }
    }

    @Override
    public ResponseEntity<String> insertStoreDetails(List<Store> storeDetails) {
        for(Store store: storeDetails)
            storeRepository.save(store);
        return new ResponseEntity<>("Inserted records successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> insertDriverDetails(List<Driver> driverDetails) {
        for(Driver driver: driverDetails)
            driverRepository.save(driver);
        return new ResponseEntity<>("Inserted records successfully!", HttpStatus.OK);
    }


}
