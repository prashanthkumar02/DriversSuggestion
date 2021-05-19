package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Return driver details example")
public class NearestDrivers {

    String driverId;
    double distanceFromStore;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getDistanceFromStore() {
        return distanceFromStore;
    }

    public void setDistanceFromStore(double distanceFromStore) {
        this.distanceFromStore = distanceFromStore;
    }
}
