package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
import com.drivers.suggestion.kafka.producer.Producer;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import com.drivers.suggestion.repositories.DriverRepository;
import com.drivers.suggestion.repositories.StoreRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class BaseServiceTest {

    private static final String STORES_SUCCESS_MESSAGE = "Inserted %d record(s) successfully!";
    private static final String DRIVERS_SUCCESS_MESSAGE = "Data published to the Kafka topic driver_location";

    @InjectMocks
    BaseService baseService;

    @Mock
    StoreRepository storeRepository;

    @Mock
    DriverRepository driverRepository;

    @Mock
    Producer producer;

    @Test
    public void testRetrieveNearestDriversFromWithSuccessData() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(SampleDataRetrieval.getSampleNearestDriversData());
        ResponseEntity<List<NearestDrivers>> expectedResponse = baseService.retrieveNearestDriversFrom("testing1", 10);

        verify(driverRepository, times(1)).getNearestDrivers(any(String.class), any(Integer.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody().size(), SampleDataRetrieval.getSampleNearestDriversData().size());
    }

    @Test(expected = GenericRestExpception.class)
    public void testGetNearestDriversWithBadData() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(new ArrayList<>());
        baseService.retrieveNearestDriversFrom("testingWithData", 10);
    }

    @Test(expected = GenericRestExpception.class)
    public void testGetNearestDriversWithNull() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(null);
        baseService.retrieveNearestDriversFrom("testingWithData", 10);
    }

    @Test(expected = GenericRestExpception.class)
    public void testGetNearestDriversWithInvalidRequestParamsOne() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(new ArrayList<>());
        baseService.retrieveNearestDriversFrom("", 10);
    }

    @Test(expected = GenericRestExpception.class)
    public void testGetNearestDriversWithInvalidRequestParamsTwo() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(new ArrayList<>());
        baseService.retrieveNearestDriversFrom(null, 10);
    }

    @Test(expected = GenericRestExpception.class)
    public void testGetNearestDriversWithInvalidRequestParamsThree() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(new ArrayList<>());
        baseService.retrieveNearestDriversFrom("Testing", 0);
    }

    @Test
    public void testInsertStoreDetails() {
        when(storeRepository.save(any(Store.class))).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        ResponseEntity<String> expectedResponse = baseService.insertStoreDetails(SampleDataRetrieval.getSampleStores());

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), String.format(STORES_SUCCESS_MESSAGE, SampleDataRetrieval.getSampleStores().size()));
    }

    @Test
    public void testInsertStoreDetailsWithNullStoreID() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);
        testingStore.setStoreID("");
        List<Store> testingStoreList = SampleDataRetrieval.getSampleStores();
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.insertStoreDetails(testingStoreList);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test(expected = GenericRestExpception.class)
    public void testInsertStoreDetailsWithNullValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);
        testingStore.setStoreID("");
        List<Store> testingStoreList = new ArrayList<>();
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.insertStoreDetails(testingStoreList);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test(expected = GenericRestExpception.class)
    public void testInsertStoreDetailsWithMisMatchValues() {
        when(storeRepository.save(any(Store.class))).thenThrow(new TransactionSystemException("Testing"));
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setStoreID("\"");
        List<Store> testingStoreList = new ArrayList<>();
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.insertStoreDetails(testingStoreList);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test(expected = GenericRestExpception.class)
    public void testInsertStoreDetailsWithExisitingValues() {
        when(storeRepository.save(any(Store.class))).thenThrow(new NullPointerException("Testing"));
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setStoreID("some value in database");
        List<Store> testingStoreList = new ArrayList<>();
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.insertStoreDetails(testingStoreList);

        verify(storeRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetails() {
        when(storeRepository.findByStoreID(any())).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        when(storeRepository.save(any(Store.class))).thenReturn(SampleDataRetrieval.getSampleStores().get(1));
        List<Store> storeList = SampleDataRetrieval.getSampleStores();
        storeList.get(1).setLatitude(-999);
        storeList.get(1).setLongitude(-100);
        ResponseEntity<String> expectedResponse = baseService.updateStoreDetails(storeList);

        verify(storeRepository, times(1)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test(expected = GenericRestExpception.class)
    public void testUpdateStoreDetailsWithNewStoreID() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = SampleDataRetrieval.getSampleStores();

        when(storeRepository.findByStoreID(any())).thenReturn(null);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);
        ResponseEntity<String> expectedResponse = baseService.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(0)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test(expected = GenericRestExpception.class)
    public void testUpdateStoreDetailsWithExistingValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = new ArrayList<>();

        when(storeRepository.findByStoreID(any())).thenReturn(testingStore);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);

        testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setLongitude(-999);
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(0)).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testUpdateStoreDetailsWithActualValues() {
        Store testingStore = SampleDataRetrieval.getSampleStores().get(1);
        List<Store> testingStoreList = new ArrayList<>();

        when(storeRepository.findByStoreID(any())).thenReturn(testingStore);
        when(storeRepository.save(any(Store.class))).thenReturn(testingStore);

        testingStore = SampleDataRetrieval.getSampleStores().get(1);
        testingStore.setLongitude(-108);
        testingStoreList.add(testingStore);
        ResponseEntity<String> expectedResponse = baseService.updateStoreDetails(testingStoreList);

        verify(storeRepository, times(testingStoreList.size())).save(any(Store.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testPublishDriverDetailsWithExisitingValues() {
        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(true);
        ResponseEntity<String> expectedResponse = baseService.publishDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), DRIVERS_SUCCESS_MESSAGE);
    }

    @Test(expected = GenericRestExpception.class)
    public void testPublishDriverDetailsWithDefaultValues() {
        Driver driver = SampleDataRetrieval.getSampleDrivers().get(1);
        driver.setLatitude(-999);
        List<Driver> driverList = new ArrayList<>();
        driverList.add(driver);
        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(false);
        ResponseEntity<String> expectedResponse = baseService.publishDriverDetails(driverList);

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testPublishDriverDetailsWithDefaultValuesTwo() {
        Driver driver = SampleDataRetrieval.getSampleDrivers().get(1);
        driver.setLatitude(-999);
        List<Driver> driverList = new ArrayList<>();
        driverList.add(driver);
        driverList.add(SampleDataRetrieval.getSampleDrivers().get(2));
        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(false);
        ResponseEntity<String> expectedResponse = baseService.publishDriverDetails(driverList);

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testInsertDriverDetailsWithExceptionsOne() {
        when(driverRepository.save(any(Driver.class))).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        when(driverRepository.findByDriverID(anyString())).thenThrow(new KafkaException("Testing"));
        baseService.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Driver.class));
    }

    @Test
    public void testInsertDriverDetailsWithExceptionsTwo() {
        when(driverRepository.save(any(Driver.class))).thenThrow(new NullPointerException());
        when(driverRepository.findByDriverID(anyString())).thenReturn(null);
        baseService.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Driver.class));
    }

    @Test
    public void testInsertDriverDetailsWithDefaultValues() {
        when(driverRepository.save(any(Driver.class))).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        when(driverRepository.findByDriverID(anyString())).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        baseService.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleDrivers().size())).save(any(Driver.class));
        verify(driverRepository, times(SampleDataRetrieval.getSampleDrivers().size())).findByDriverID(any());
    }
}