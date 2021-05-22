package com.drivers.suggestion.service;

import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDriverService {
    ResponseEntity<ResponseBody> retrieveNearestDriversFrom(String storeID, int numOfDrivers);
    ResponseEntity<ResponseBody> publishDriverDetails(List<Driver> driverDetails);
    void insertDriverDetails(List<Driver> driverList);
}
