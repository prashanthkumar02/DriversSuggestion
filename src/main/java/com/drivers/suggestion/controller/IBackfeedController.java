package com.drivers.suggestion.controller;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.NoDataFoundException;
import com.drivers.suggestion.model.Driver;
import com.drivers.suggestion.model.NearestDrivers;
import com.drivers.suggestion.model.Store;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IBackfeedController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success in retrieving numOfDrivers data."),
            @ApiResponse(code = 201, message = "No data available.")
    })
    @ApiOperation(value = "Retrieves 'numofDrivers' nearest drivers location",
            response = NearestDrivers.class,
            responseContainer = "List")
    @GetMapping("/fetchNearestDrivers")
    ResponseEntity<List<NearestDrivers>> getNearestDrivers(
            @ApiParam(
                    value = "Store number to fetch the nearest drivers",
                    type = "String",
                    example = "1234",
                    required = true) String storeId,
            @ApiParam(
                    value = "Number drivers to be fetched",
                    type = "Integer",
                    example = "10",
                    required = true) int numOfDrivers) throws NoDataFoundException;


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gives success message upon inserting the records in database"),
            @ApiResponse(code = 400, message = "Bad JSON Request")
    })
    @ApiOperation(value = "Retrieves stores data", response = String.class)
    @PostMapping("/StoresData")
    ResponseEntity<String> postLoadOfStoreDetails(
            @ApiParam(
                    value = "List of store details",
                    type = "List<Store>",
                    required = true) @RequestBody List<Store> storesConfig);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gives success message upon inserting drivers data into database") })
    @ApiOperation(value = "Back feeding the drivers data into the database", response = String.class)
    @PostMapping("/driversData")
    ResponseEntity<String> postDriversData(
            @ApiParam(
                    value = "List of drivers details",
                    type = "List<Driver>",
                    required = true) @RequestBody List<Driver> driversData);
}
