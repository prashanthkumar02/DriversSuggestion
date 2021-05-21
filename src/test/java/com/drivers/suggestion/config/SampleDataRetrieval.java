package com.drivers.suggestion.config;

import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;

import java.util.ArrayList;
import java.util.List;

public class SampleDataRetrieval {

     public static List<NearestDrivers> getSampleNearestDriversData() {
        List<NearestDrivers> nearestDrivers = new ArrayList<>();

        NearestDrivers driver = new NearestDrivers("testing1", 90, 180, 100);
        nearestDrivers.add(driver);

        driver = new NearestDrivers("testing2", 0, 0, 0);
        nearestDrivers.add(driver);

        driver = new NearestDrivers("testing3", -90, -180, 100);
        nearestDrivers.add(driver);

        return nearestDrivers;
    }

    public static  List<Store> getSampleStores() {
         List<Store> storeLists = new ArrayList<>();
         Store store = new Store();

         store.setStoreID("testing1");
         store.setLatitude(90);
         store.setLongitude(180);
         storeLists.add(store);

         store.setStoreID("testing2");
         store.setLatitude(0);
         store.setLongitude(0);
         storeLists.add(store);

         store.setStoreID("testing3");
         store.setLatitude(-90);
         store.setLongitude(-180);
         storeLists.add(store);
         return storeLists;
    }

    public static List<Driver> getSampleDrivers() {
        List<Driver> driverList = new ArrayList<>();
        Driver driver = new Driver();

        driver.setDriverID("testing1");
        driver.setLatitude(90);
        driver.setLongitude(180);
        driverList.add(driver);

        driver.setDriverID("testing2");
        driver.setLatitude(0);
        driver.setLongitude(0);
        driverList.add(driver);

        driver.setDriverID("testing3");
        driver.setLatitude(-90);
        driver.setLongitude(-180);
        driverList.add(driver);

        return driverList;
    }
}
