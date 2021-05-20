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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class BaseServiceTest {

    private static final String STORES_SUCCESS_MESSAGE = "Inserted records successfully!";
    private static final String DRIVERS_SUCCESS_MESSAGE = "Published in Kafka topic driver_location";

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
        Assertions.assertEquals(expectedResponse.getBody(), STORES_SUCCESS_MESSAGE);
    }

    @Test
    public void testInsertDriverDetails() {
        when(driverRepository.save(any(Driver.class))).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        baseService.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Driver.class));
    }

    @Test
    public void testPublishDriverDetails() {
        doNothing().when(producer).sendDriversData(anyList());
        ResponseEntity<String> expectedResponse = baseService.publishDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), DRIVERS_SUCCESS_MESSAGE);
    }
}