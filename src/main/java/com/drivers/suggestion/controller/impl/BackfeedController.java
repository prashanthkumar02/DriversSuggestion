package com.drivers.suggestion.controller.impl;

import com.drivers.suggestion.controller.IBackfeedController;
import com.drivers.suggestion.controller.exceptionHandler.exceptions.NoDataFoundException;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BackfeedController implements IBackfeedController {

    @Autowired
    IBaseService baseService;

    @Override
    public ResponseEntity<List<NearestDrivers>> getNearestDrivers(String storeId, int numOfDrivers) throws NoDataFoundException {
        return baseService.retrieveNearestDriversFrom(storeId, numOfDrivers);
    }

    @Override
    public ResponseEntity<String> postLoadOfStoreDetails(List<Store> storesConfig) {
        return baseService.insertStoreDetails(storesConfig);
    }

    @Override
    public ResponseEntity<String> postDriversData(List<Driver> driversData) {
        return baseService.insertDriverDetails(driversData);
    }

}
