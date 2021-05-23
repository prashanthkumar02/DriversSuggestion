package com.drivers.suggestion.service.impl;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.kafka.producer.Producer;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.repositories.DriverRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
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
public class DriverServiceImplTest {

    private final static String DATA_PUBLISHED_TO_TOPIC = "%d record(s) published to the topic driver_location";

    @InjectMocks
    DriverServiceImpl driverServiceImpl;

    @Mock
    DriverRepository driverRepository;

    @Mock
    Producer producer;

    @Test
    public void testRetrieveNearestDriversFromWithSuccessData() {
        when(driverRepository.getNearestDrivers(any(String.class), any(Integer.class))).thenReturn(SampleDataRetrieval.getSampleNearestDriversData());
        ResponseEntity<ResponseBody> expectedResponse = driverServiceImpl.retrieveNearestDriversFrom("testing1", 10);

        verify(driverRepository, times(1)).getNearestDrivers(any(String.class), any(Integer.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody().getDriversFetched().size(), SampleDataRetrieval.getSampleNearestDriversData().size());
    }


    @Test
    public void testPublishDriverDetailsWithExistingValues() throws JsonProcessingException {
        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(true);
        ResponseEntity<ResponseBody> expectedResponse = driverServiceImpl.publishDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), String.format(DATA_PUBLISHED_TO_TOPIC, SampleDataRetrieval.getSampleDrivers().size()));
    }

    @Test
    public void testPublishDriverDetailsWithDefaultValues() throws JsonProcessingException {
        Driver driver = SampleDataRetrieval.getSampleDrivers().get(1);
        driver.setLatitude(-999.00);
        List<Driver> driverList = new ArrayList<>();
        driverList.add(driver);

        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(false);
        ResponseEntity<ResponseBody> expectedResponse = driverServiceImpl.publishDriverDetails(driverList);

        verify(producer, times(0)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testPublishDriverDetailsWithDefaultValuesTwo() throws JsonProcessingException {
        Driver driver = SampleDataRetrieval.getSampleDrivers().get(1);
        driver.setLatitude(-999.00);
        List<Driver> driverList = new ArrayList<>();
        driverList.add(driver);
        driverList.add(SampleDataRetrieval.getSampleDrivers().get(2));
        doNothing().when(producer).sendDriversData(anyList());
        when(driverRepository.existsByDriverID(anyString())).thenReturn(false);
        ResponseEntity<ResponseBody> expectedResponse = driverServiceImpl.publishDriverDetails(driverList);

        verify(producer, times(1)).sendDriversData(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 202);
        Assertions.assertNotNull(expectedResponse.getBody());
    }

    @Test
    public void testInsertDriverDetailsWithDefaultValues() {
        when(driverRepository.save(any(Driver.class))).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        when(driverRepository.findByDriverID(anyString())).thenReturn(SampleDataRetrieval.getSampleDrivers().get(1));
        driverServiceImpl.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleDrivers().size())).save(any(Driver.class));
        verify(driverRepository, times(SampleDataRetrieval.getSampleDrivers().size())).findByDriverID(any());
    }

    @Test
    public void testInsertDriverDetailsWithExceptions() {
        when(driverRepository.save(any(Driver.class))).thenThrow(new NullPointerException());
        when(driverRepository.findByDriverID(anyString())).thenReturn(null);
        driverServiceImpl.insertDriverDetails(SampleDataRetrieval.getSampleDrivers());

        verify(driverRepository, times(SampleDataRetrieval.getSampleStores().size())).save(any(Driver.class));
    }
}