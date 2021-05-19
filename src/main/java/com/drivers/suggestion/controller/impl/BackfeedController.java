package com.drivers.suggestion.controller.impl;

import com.drivers.suggestion.controller.IBackfeedController;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BackfeedController implements IBackfeedController {

    @Override
    public List<NearestDrivers> getNearestDrivers(String storeId, int numOfDrivers) {
        System.out.println("Store ID passed - " + storeId + " with " + numOfDrivers + " drivers needed.");
        return null;
    }

    @Override
    public void getStoreDetails(List<Store> storesConfig) {

    }

    @Override
    public void postDriversData(List<Driver> driversData) {

    }

}
