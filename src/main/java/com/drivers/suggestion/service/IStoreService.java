package com.drivers.suggestion.service;

import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.model.Store;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IStoreService {
    ResponseEntity<ResponseBody> insertStoreDetails(List<Store> storeDetails, int storesInBadFormat);
    ResponseEntity<ResponseBody> updateStoreDetails(List<Store> storeDetails);
}
