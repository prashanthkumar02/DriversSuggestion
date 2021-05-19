package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Driver object sample")
public class Driver {

    String driverId;
    double driversLatitude;
    double driversLongitude;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getDriversLatitude() {
        return driversLatitude;
    }

    public void setDriversLatitude(double driversLatitude) {
        this.driversLatitude = driversLatitude;
    }

    public double getDriversLongitude() {
        return driversLongitude;
    }

    public void setDriversLongitude(double driversLongitude) {
        this.driversLongitude = driversLongitude;
    }
}
