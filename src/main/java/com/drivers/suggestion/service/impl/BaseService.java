package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.constants.Constants;
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
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.ArrayList;
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
        if(storeID == null || storeID.trim().isEmpty())
            throw new GenericRestExpception("storeID cannot be null or empty", HttpStatus.BAD_REQUEST);
        if(numOfDrivers < 1)
            throw new GenericRestExpception("numOfDrivers cannot be less than 1", HttpStatus.BAD_REQUEST);

        List<NearestDrivers> closestDrivers = driverRepository.getNearestDrivers(storeID.trim(), numOfDrivers);
        if(closestDrivers == null || closestDrivers.isEmpty())
            throw new GenericRestExpception("No data found for storeId - " + storeID, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(closestDrivers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> insertStoreDetails(List<Store> storeDetails) {
        int numOfStoresExist = 0;
        int numOfStoreInBadFormat = 0;
        for(Store store: storeDetails) {
            try {
                if(store.getStoreID() == null
                        || store.getStoreID().trim().isEmpty()
                        || store.getLatitude() == Constants.DEFAULT_VALUE
                        || store.getLongitude() == Constants.DEFAULT_VALUE) {
                    numOfStoreInBadFormat++;
                } else {
                    storeRepository.save(store);
                }
            } catch (TransactionSystemException ex) {
                numOfStoreInBadFormat++;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                numOfStoresExist++;
            }
        }

        if(numOfStoresExist == storeDetails.size())
            throw new GenericRestExpception("No new record to insert, please use PUT endpoint to update (or) Enter new records to insert", HttpStatus.NOT_ACCEPTABLE);
        if(numOfStoreInBadFormat == storeDetails.size())
            throw new GenericRestExpception("There must be one of the following, please check the following - All fields are mandatory, fields might having improper data", HttpStatus.NOT_ACCEPTABLE);
        if(numOfStoresExist > 0 || numOfStoreInBadFormat > 0)
            return new ResponseEntity<>(  "Inserted " + (storeDetails.size() - numOfStoresExist - numOfStoreInBadFormat) + " record(s)\n" + numOfStoresExist + " duplicate(s) found, please use PUT endpoint to update.\n" + numOfStoreInBadFormat + " has bad format, please verify all fields are exists", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Inserted " + storeDetails.size() + " record(s) successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateStoreDetails(List<Store> storeDetails) {
        int numSameStores = 0;
        int numNewStores = 0;
        for(Store store : storeDetails) {
            Store updatedStore = storeRepository.findByStoreID(store.getStoreID());
            if(updatedStore != null) {
                if(store.getLatitude() == Constants.DEFAULT_VALUE)
                    store.setLatitude(updatedStore.getLatitude());
                if(store.getLongitude() == Constants.DEFAULT_VALUE)
                    store.setLongitude(updatedStore.getLongitude());
            }

            if(updatedStore != null && !updatedStore.equals(store)) {
                updatedStore.setLatitude(store.getLatitude());
                updatedStore.setLongitude(store.getLongitude());
                storeRepository.save(updatedStore);
            } else {
                if(updatedStore == null)
                    numNewStores++;
                else numSameStores++;
            }
        }

        if(numSameStores < 1 && numNewStores < 1)
            return new ResponseEntity<>("Updated " + storeDetails.size() + " record(s) successfully", HttpStatus.OK);
        if(numSameStores == storeDetails.size())
            throw new GenericRestExpception(numSameStores + " record(s) has been found identical (or) no data passed, aborting update!", HttpStatus.NOT_ACCEPTABLE);
        if(numNewStores == storeDetails.size())
            throw new GenericRestExpception(numNewStores + " new record(s), please use POST method ", HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>("Updated " + (storeDetails.size() - numSameStores - numNewStores) + " record(s) successfully\n"
                + numSameStores + " record(s) matches the database.\n"
                + numNewStores + " new record(s) found, use POST method.", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> publishDriverDetails(List<Driver> driverDetails) {
        List<Driver> driversDetailsProperDetails = new ArrayList<>();
        int numImproperDataFormat = 0;
        for(Driver driver: driverDetails) {
            if (driverRepository.existsByDriverID(driver.getDriverID()))
                driversDetailsProperDetails.add(driver);
            else {
                if (driver.getLatitude() == Constants.DEFAULT_VALUE || driver.getLongitude() == Constants.DEFAULT_VALUE)
                    numImproperDataFormat++;
                else if (driverRepository.findByDriverID(driver.getDriverID()) == null)
                    driversDetailsProperDetails.add(driver);
            }
        }
        this.producer.sendDriversData(driversDetailsProperDetails);
        if(numImproperDataFormat == driverDetails.size())
            throw new GenericRestExpception("Dataset might have following problems, please check if data has missing fields (new record)", HttpStatus.NOT_ACCEPTABLE);
        if(numImproperDataFormat > 0)
            return new ResponseEntity<>((driverDetails.size() - numImproperDataFormat) + " record(s) are published to Kafka topic driver_location\n" + numImproperDataFormat + " record(s) has improper structure please verify before retrying", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Data published to the Kafka topic driver_location", HttpStatus.OK);
    }

    @Override
    public void insertDriverDetails(List<Driver> driverList) {
        for(Driver driver: driverList) {
            Driver updatedDriver = null;
            try {
                updatedDriver = driverRepository.findByDriverID(driver.getDriverID());
            } catch(KafkaException ex) {
                System.out.println("Ignored Exception");
            }
            if(updatedDriver != null) {
                if(driver.getLatitude() != Constants.DEFAULT_VALUE)
                    updatedDriver.setLatitude(driver.getLatitude());
                if(driver.getLongitude() != Constants.DEFAULT_VALUE)
                    updatedDriver.setLongitude(driver.getLongitude());
                driverRepository.save(updatedDriver);
            } else {
                try {
                    driverRepository.save(driver);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Unable to insert/update the data, please check the values before retry driverID - " + driver.getDriverID());
                }
            }
        }
    }

}
