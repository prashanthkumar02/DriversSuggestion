package com.drivers.suggestion.controller.impl;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.service.impl.BaseService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class BackfeedControllerTest {

    @InjectMocks
    BackfeedController backfeedController;

    @Mock
    BaseService baseService;

    @Test
    public void testGetNearestDrivers() {
        when(baseService.retrieveNearestDriversFrom(any(String.class), any(Integer.class))).thenReturn(new ResponseEntity<>(SampleDataRetrieval.getSampleNearestDriversData(), HttpStatus.OK));
        ResponseEntity<List<NearestDrivers>> expectedResponse = backfeedController.getNearestDrivers("testing1", 10);

        verify(baseService, times(1)).retrieveNearestDriversFrom(any(String.class), any(Integer.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody().size(), SampleDataRetrieval.getSampleNearestDriversData().size());
    }

    @Test
    public void testPostLoadOfStoreDetails() {
        String body = "Inserted records successfully!";
        when(baseService.insertStoreDetails(anyList())).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));
        ResponseEntity<String> expectedResponse = backfeedController.postLoadOfStoreDetails(SampleDataRetrieval.getSampleStores());

        verify(baseService, times(1)).insertStoreDetails(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), body);
    }

    @Test
    public void testPutLoadOfStoreDetails() {
        String body = "Updated records successfully!";
        when(baseService.updateStoreDetails(anyList())).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));
        ResponseEntity<String> expectedResponse = backfeedController.putLoadOfStoreDetails(SampleDataRetrieval.getSampleStores());

        verify(baseService, times(1)).updateStoreDetails(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), body);
    }

    @Test
    public void testPublishDriverDetails() {
        String body = "Published records successfully!";
        when(baseService.publishDriverDetails(anyList())).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));
        ResponseEntity<String> expectedResponse = backfeedController.postDriversData(SampleDataRetrieval.getSampleDrivers());

        verify(baseService, times(1)).publishDriverDetails(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertEquals(expectedResponse.getBody(), body);
    }
}