package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.repositories.StoreRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class StoreServiceImplTest {

    @InjectMocks
    StoreServiceImpl storeServiceImpl;

    @Mock
    StoreRepository storeRepository;

    @Test
    public void testInsertStoreDetails() {
        when(storeRepository.save(any(Store.class))).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.insertStoreDetails(SampleDataRetrieval.getSampleStores(), 0);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
    }

    @Test
    public void testInsertStoreDetailsWithMixOfValues() {
        when(storeRepository.save(any(Store.class))).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.insertStoreDetails(SampleDataRetrieval.getSampleStores(), 2);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
    }

    @Test
    public void testInsertStoreDetailsWithNullValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);
        testingStore.setStoreID("");
        List<Store> testingStoreList = new ArrayList<>();
        testingStoreList.add(testingStore);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.insertStoreDetails(testingStoreList, 0);

        verify(storeRepository, times(testingStoreList.size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testInsertStoreDetailsWithExistingValues() {
        when(storeRepository.save(any(Store.class))).thenThrow(new NullPointerException("Testing"));
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setStoreID("some value in database");
        List<Store> testingStoreList = new ArrayList<>();
        testingStoreList.add(testingStore);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.insertStoreDetails(testingStoreList, 0);

        verify(storeRepository, times(testingStoreList.size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetails() {
        when(storeRepository.findByStoreID(any())).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        when(storeRepository.save(any(Store.class))).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        List<Store> storeList = SampleDataRetrieval.getSampleStores();
        storeList.get(1).setLatitude(-999.00);
        storeList.get(1).setLongitude(-100.00);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.updateStoreDetails(storeList);

        verify(storeRepository, times(1)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetailsWithActualValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = new ArrayList<>();

        when(storeRepository.findByStoreID(any())).thenReturn(testingStore);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);

        testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setLongitude(-108.00);
        testingStoreList.add(testingStore);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(testingStoreList.size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetailsWithNewStoreID() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = SampleDataRetrieval.getSampleStores();

        when(storeRepository.findByStoreID(any())).thenReturn(null);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(0)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetailsWithExistingValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = new ArrayList<>();

        when(storeRepository.findByStoreID(any())).thenReturn(testingStore);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);

        testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setLongitude(-999.00);
        testingStoreList.add(testingStore);
        ResponseEntity<ResponseBody> expectedResponse = storeServiceImpl.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(0)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }
}