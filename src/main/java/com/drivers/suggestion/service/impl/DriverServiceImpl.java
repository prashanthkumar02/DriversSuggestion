package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.constants.Constants;
import com.drivers.suggestion.kafka.producer.Producer;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.repositories.DriverRepository;
import com.drivers.suggestion.service.IDriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DriverServiceImpl implements IDriverService {

    private final DriverRepository driverRepository;
    private final Producer producer;

    private final static String NO_DATA_FOUND = "No data found for storeId - %s";
    private final static String DATA_FOUND = "%d record(s) have found, sorted upon the closet distance";
    private final static String DATA_PUBLISHED_TO_TOPIC = "%d records are published to the topic driver_location";
    private final static String DATA_WITH_MISSING_FIELDS = "%d record(s) has a missing fields (for new record)";
    private static final String UNHANDLED_EXCEPTION = "Unhandled JSON Exception! further message - %s";

    @Override
    public ResponseEntity<ResponseBody> retrieveNearestDriversFrom(String storeID, int numOfDrivers) {
        ResponseBody responseBody = responseBodyBuild(HttpStatus.NOT_FOUND.name(), String.format(NO_DATA_FOUND, storeID));

        List<NearestDrivers> closestDrivers = driverRepository.getNearestDrivers(storeID.trim(), numOfDrivers);
        if(closestDrivers != null && !closestDrivers.isEmpty()) {
            responseBody.setStatus(HttpStatus.OK.name());
            responseBody.setMessage(String.format(DATA_FOUND, closestDrivers.size()));
            responseBody.setDriversFetched(closestDrivers);
        }

        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(responseBody.getStatus()));
    }


    @Override
    public ResponseEntity<ResponseBody> publishDriverDetails(List<Driver> driverDetails) {
        ResponseBody responseBody = responseBodyBuild(HttpStatus.OK.name(),String.format(DATA_PUBLISHED_TO_TOPIC, driverDetails.size()));

        List<Driver> driversDetailsProperDetails;
        driversDetailsProperDetails = driverDetails.stream()
                .map(driver -> {
                     if(driverRepository.existsByDriverID(driver.getDriverID()))
                         return driver;
                    if(driver.getLatitude() == Constants.DEFAULT_VALUE || driver.getLongitude() == Constants.DEFAULT_VALUE)
                        return null;
                    return driver;
                }).filter(Objects::nonNull).collect(Collectors.toList());

        try {
            if(!driversDetailsProperDetails.isEmpty())
                this.producer.sendDriversData(driversDetailsProperDetails);
        } catch(JsonProcessingException ex) {
            responseBody = responseBodyBuild(HttpStatus.BAD_REQUEST.name(), String.format(UNHANDLED_EXCEPTION, ex.getMessage()));
        }


        if(driversDetailsProperDetails.size() == 0)
            responseBody = responseBodyBuild(HttpStatus.NOT_ACCEPTABLE.name(), String.format(DATA_WITH_MISSING_FIELDS, driverDetails.size()));
        else if(driversDetailsProperDetails.size() < driverDetails.size())
            responseBody = responseBodyBuild(HttpStatus.ACCEPTED.name(),
                                String.format(DATA_PUBLISHED_TO_TOPIC, driverDetails.size() - driversDetailsProperDetails.size())
                                        + String.format(DATA_WITH_MISSING_FIELDS, driversDetailsProperDetails.size()));
        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(responseBody.getStatus()));
    }

    @Override
    public void insertDriverDetails(List<Driver> driverList) {
        for(Driver driver: driverList) {
            Driver updatedDriver = driverRepository.findByDriverID(driver.getDriverID());
            if(updatedDriver != null) {
                if(driver.getLatitude() != Constants.DEFAULT_VALUE)
                    updatedDriver.setLatitude(driver.getLatitude());
                if(driver.getLongitude() != Constants.DEFAULT_VALUE)
                    updatedDriver.setLongitude(driver.getLongitude());
                driverRepository.save(updatedDriver);
            } else {
                try {
                    driverRepository.save(driver);
                } catch(Exception ignored) {}
            }
        }
    }

    private ResponseBody responseBodyBuild(String status, String message) {
        return ResponseBody.builder()
                .status(status)
                .message(message).build();
    }

}
