package com.drivers.suggestion.controller;

import com.drivers.suggestion.controller.exceptionHandler.exceptions.GenericRestExpception;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IBackfeedController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success in retrieving numOfDrivers data."),
            @ApiResponse(code = 400, message = "Invalid query parameters"),
            @ApiResponse(code = 404, message = "No Data found for given storeID"),
            @ApiResponse(code = 500, message = "Internal server error")
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
                    required = true) int numOfDrivers) throws GenericRestExpception;


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns this response only if all the below requirements are met:" +
                    "\nInserting all the records into database." +
                    "\nNo duplicate(s) found in the given dataset."),
            @ApiResponse(code = 202, message = "Success upon inserting new records." +
                    "\nand if any duplicate(s) found in the given dataset, founded records won't be inserted"),
            @ApiResponse(code = 400, message = "Bad JSON Request"),
            @ApiResponse(code = 406, message = "Returns this if the given dataset itself is a duplicate set" +
                    ", having any invalid field values" +
                    ", and any field value is missing"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Insert any new store data", response = String.class)
    @PostMapping("/postStoresData")
    ResponseEntity<String> postLoadOfStoreDetails(
            @ApiParam(
                    value = "List of store details",
                    type = "List<Store>",
                    required = true) @RequestBody List<Store> storesConfig);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success in updating all the records."),
            @ApiResponse(code = 202, message = "Success upon updating new records." +
                    "\nand if any record(s) have identical field values exists in database, these record(s) won't update"),
            @ApiResponse(code = 400, message = "Bad JSON Request"),
            @ApiResponse(code = 406, message = "Returns this if there is no need for updating"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Update any existing store data", response = String.class)
    @PutMapping("/putStoresData")
    ResponseEntity<String> putLoadOfStoreDetails(
            @ApiParam(
                    value = "List of store details",
                    type = "List<Store>",
                    required = true) @RequestBody List<Store> storesConfig);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success if all the record(s) in format(new or existing)."),
            @ApiResponse(code = 202, message = "All the formatted record(s) will be published to Kafka topic, and unformatted record(s) will not be published."),
            @ApiResponse(code = 400, message = "Bad JSON Request"),
            @ApiResponse(code = 406, message = "Returns this if non the record(s) are formatted."),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Back feeding the drivers data into the database using Kafka, REST", response = String.class)
    @PostMapping("/postDriversData")
    ResponseEntity<String> postDriversData(
            @ApiParam(
                    value = "List of drivers details",
                    type = "List<Driver>",
                    required = true) @RequestBody List<Driver> driversData);
}
