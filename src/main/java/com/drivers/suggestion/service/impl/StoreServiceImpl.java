package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.constants.Constants;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.repositories.StoreRepository;
import com.drivers.suggestion.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements IStoreService {

    @Autowired
    StoreRepository storeRepository;

    private static final String MESSAGE_INSERTED_RECORDS = "Inserted %d record(s)! ";
    private static final String MESSAGE_BAD_FORMAT_RECORDS = "%d has bad format, All fields are mandatory ";
    private static final String MESSAGE_EXISTING_RECORDS = "%d record(s) found in database, please use PUT endpoint to update (or) Enter new records to insert ";

    private static final String MESSAGE_ON_SUCCESSFUL_UPDATE = "Updated %d record(s)!";
    private static final String MESSAGE_FOR_NEW_STORES_UPDATE = "%d record(s) has been found identical";
    private static final String MESSAGE_FOR_IDENTICAL_STORES_UPDATE = "%d new record(s) found, please use POST method";

    @Override
    public ResponseEntity<ResponseBody> insertStoreDetails(List<Store> storeDetails, int storesInBadFormat) {
        ResponseBody responseBody = responseBodyBuild(HttpStatus.OK.name(), String.format(MESSAGE_INSERTED_RECORDS, storeDetails.size()));
        long storesAlreadyExist
                = storeDetails
                        .stream()
                        .map((store) -> {
                            boolean inserted = false;
                            try {
                                storeRepository.save(store);
                                inserted = true;
                            } catch (Exception ignored) {}
                            return inserted;
                        }).filter((value) -> !value).count();

        if(storesAlreadyExist > 0 || storesInBadFormat > 0) {
            if(storesAlreadyExist == storeDetails.size() + storesInBadFormat) {
                responseBody = responseBodyBuild(HttpStatus.NOT_ACCEPTABLE.name(), String.format(MESSAGE_EXISTING_RECORDS, storesAlreadyExist));
            } else {
                responseBody = responseBodyBuild(HttpStatus.ACCEPTED.name(),
                        String.format(MESSAGE_INSERTED_RECORDS, (storeDetails.size() - storesAlreadyExist))
                        + String.format(MESSAGE_BAD_FORMAT_RECORDS, storesInBadFormat)
                        + String.format(MESSAGE_EXISTING_RECORDS, storesAlreadyExist));
            }
        }

        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(responseBody.getStatus()));
    }

    @Override
    public ResponseEntity<ResponseBody> updateStoreDetails(List<Store> storeDetails) {
        int numSameStores = 0;
        int numNewStores = 0;
        ResponseBody responseBody = responseBodyBuild(HttpStatus.OK.name(), String.format(MESSAGE_ON_SUCCESSFUL_UPDATE, storeDetails.size()));
        for(Store store : storeDetails) {
            Store updatedStore = storeRepository.findByStoreID(store.getStoreID());
            if(updatedStore != null) {
                if(store.getLatitude() == Constants.DEFAULT_VALUE)
                    store.setLatitude(updatedStore.getLatitude());
                if(store.getLongitude() == Constants.DEFAULT_VALUE)
                    store.setLongitude(updatedStore.getLongitude());
            }

            if(updatedStore == null)
                numNewStores++;
            else if(updatedStore.equals(store))
                numSameStores++;
            else {
                updatedStore.setLatitude(store.getLatitude());
                updatedStore.setLongitude(store.getLongitude());
                storeRepository.save(updatedStore);
            }

        }


        if(numSameStores > 0 || numNewStores > 0) {
            if (numSameStores == storeDetails.size()) {
                responseBody = responseBodyBuild(HttpStatus.NOT_ACCEPTABLE.name(), String.format(MESSAGE_FOR_IDENTICAL_STORES_UPDATE, numSameStores));
            } else if (numNewStores == storeDetails.size()) {
                responseBody = responseBodyBuild(HttpStatus.NOT_ACCEPTABLE.name(), String.format(MESSAGE_FOR_NEW_STORES_UPDATE, numSameStores));
            } else {
                responseBody = responseBodyBuild(HttpStatus.ACCEPTED.name(),
                        String.format(MESSAGE_ON_SUCCESSFUL_UPDATE, (storeDetails.size() - numSameStores - numNewStores))
                        + String.format(MESSAGE_FOR_IDENTICAL_STORES_UPDATE, numSameStores)
                        + String.format(MESSAGE_FOR_NEW_STORES_UPDATE, numNewStores));
            }
        }

        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(responseBody.getStatus()));
    }

    private ResponseBody responseBodyBuild(String status, String message) {
        return ResponseBody.builder()
                .status(status)
                .message(message).build();
    }
}
