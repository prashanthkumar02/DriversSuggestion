package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
import com.drivers.suggestion.kafka.producer.Producer;
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

    @Autowired
    private Producer producer;

    @Override
    public ResponseEntity<List<NearestDrivers>> retrieveNearestDriversFrom(String storeID, int numOfDrivers) throws GenericRestExpception {
        if(storeID == null || storeID.isEmpty()) {
            throw new GenericRestExpception("storeID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        if(numOfDrivers < 1) {
            throw new GenericRestExpception("numOfDrivers cannot be less than 1", HttpStatus.BAD_REQUEST);
        }

        List<NearestDrivers> closestDrivers = driverRepository.getNearestDrivers(storeID, numOfDrivers);
        if(closestDrivers == null || closestDrivers.isEmpty()) {
            throw new GenericRestExpception("No data found for storeId - " + storeID, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(closestDrivers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> insertStoreDetails(List<Store> storeDetails) {
        for(Store store: storeDetails)
            storeRepository.save(store);
        return new ResponseEntity<>("Inserted records successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> publishDriverDetails(List<Driver> driverDetails) {
        this.producer.sendDriversData(driverDetails);
        return new ResponseEntity<>("Published in Kafka topic driver_location", HttpStatus.OK);
    }

    @Override
    public void insertDriverDetails(List<Driver> driverList) {
        for(Driver driver: driverList)
            driverRepository.save(driver);
    }

}
