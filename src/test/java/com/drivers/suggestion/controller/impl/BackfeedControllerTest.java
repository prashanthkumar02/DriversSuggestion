package com.drivers.suggestion.controller.impl;

import com.drivers.suggestion.config.SampleDataRetrieval;
import com.drivers.suggestion.model.requestAndResponse.ResponseBody;
import com.drivers.suggestion.service.impl.DriverServiceImpl;
import com.drivers.suggestion.service.impl.StoreServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class BackfeedControllerTest {

    @InjectMocks
    BackfeedController backfeedController;

    @Mock
    DriverServiceImpl driverServiceImpl;

    @Mock
    StoreServiceImpl storeServiceImpl;

    @Test
    public void testGetNearestDrivers() {
        String body = "Inserted records successfully!";
        ResponseBody responseBody = ResponseBody.builder()
                                        .status(HttpStatus.OK.name())
                                        .message(body)
                                        .driversFetched(SampleDataRetrieval.getSampleNearestDriversData()).build();
        when(driverServiceImpl.retrieveNearestDriversFrom(any(String.class), any(Integer.class))).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));
        ResponseEntity<ResponseBody> expectedResponse = backfeedController.getNearestDrivers("testing1", 10);

        verify(driverServiceImpl, times(1)).retrieveNearestDriversFrom(any(String.class), any(Integer.class));
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
        Assertions.assertNotNull(expectedResponse.getBody().getDriversFetched());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), body);
        Assertions.assertEquals(expectedResponse.getBody().getDriversFetched().size(), SampleDataRetrieval.getSampleNearestDriversData().size());
    }

    @Test
    public void testPostLoadOfStoreDetails() {
        String body = "Inserted records successfully!";
        when(storeServiceImpl.insertStoreDetails(anyList(), anyInt())).thenReturn(responseBodyBuilder(body));
        ResponseEntity<ResponseBody> expectedResponse = backfeedController.postLoadOfStoreDetails(SampleDataRetrieval.getSampleStores());

        verify(storeServiceImpl, times(1)).insertStoreDetails(anyList(), anyInt());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), body);
    }

    @Test
    public void testPostLoadOfStoreDetailsWithImproperData() {
        String body = "3 record(s) are improper, All field values are mandatory";
        ResponseEntity<ResponseBody> expectedResponse = backfeedController.postLoadOfStoreDetails(SampleDataRetrieval.getSampleDefaultStores());

        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 406);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), body);
    }

    @Test
    public void testPutLoadOfStoreDetails() {
        String body = "Updated records successfully!";
        when(storeServiceImpl.updateStoreDetails(anyList())).thenReturn(responseBodyBuilder(body));
        ResponseEntity<ResponseBody> expectedResponse = backfeedController.putLoadOfStoreDetails(SampleDataRetrieval.getSampleStores());

        verify(storeServiceImpl, times(1)).updateStoreDetails(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), body);
    }

    @Test
    public void testPublishDriverDetails() {
        String body = "Published records successfully!";
        when(driverServiceImpl.publishDriverDetails(anyList())).thenReturn(responseBodyBuilder(body));
        ResponseEntity<ResponseBody> expectedResponse = backfeedController.postDriversData(SampleDataRetrieval.getSampleDrivers());

        verify(driverServiceImpl, times(1)).publishDriverDetails(anyList());
        Assertions.assertEquals(expectedResponse.getStatusCodeValue(), 200);
        Assertions.assertNotNull(expectedResponse.getBody());
        Assertions.assertNotNull(expectedResponse.getBody().getMessage());
        Assertions.assertEquals(expectedResponse.getBody().getMessage(), body);
    }

    private ResponseEntity<ResponseBody> responseBodyBuilder(String body) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.OK.name())
                .message(body).build();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}