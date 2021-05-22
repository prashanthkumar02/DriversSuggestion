package com.drivers.suggestion.controller.impl;

import com.drivers.suggestion.constants.Constants;
import com.drivers.suggestion.controller.IBackfeedController;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.service.IDriverService;
import com.drivers.suggestion.service.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BackfeedController implements IBackfeedController {

    private final IDriverService driverService;
    private final IStoreService storeService;

    private final String GENERIC_MESSAGE_FOR_INSERT_STORE = "%d record(s) are improper, All field values are mandatory";

    @Override
    public ResponseEntity<ResponseBody> getNearestDrivers(String storeId, int numOfDrivers) {
        return driverService.retrieveNearestDriversFrom(storeId, numOfDrivers);
    }

    @Override
    public ResponseEntity<ResponseBody> postDriversData(List<Driver> driversData) {
        return driverService.publishDriverDetails(driversData);
    }

    @Override
    public ResponseEntity<ResponseBody> postLoadOfStoreDetails(List<Store> storesConfig) {
        List<Store> filteredStores = storesConfig.stream()
                .filter((store) ->
                        !(store.getLatitude() == Constants.DEFAULT_VALUE
                        || store.getLongitude() == Constants.DEFAULT_VALUE))
                .collect(Collectors.toList());
        if(filteredStores.isEmpty())
            return new ResponseEntity<>(ResponseBody.builder()
                    .status(HttpStatus.NOT_ACCEPTABLE.name())
                    .message(String.format(GENERIC_MESSAGE_FOR_INSERT_STORE, storesConfig.size()))
                    .build(), HttpStatus.NOT_ACCEPTABLE);
        return storeService.insertStoreDetails(filteredStores, storesConfig.size() - filteredStores.size());
    }

    @Override
    public ResponseEntity<ResponseBody> putLoadOfStoreDetails(List<Store> storesConfig) {
        return storeService.updateStoreDetails(storesConfig);
    }

}
