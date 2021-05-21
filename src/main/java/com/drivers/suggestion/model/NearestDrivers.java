package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Return driver details example")
public class NearestDrivers {

    String driverID;
    double latitude;
    double longitude;
    double distanceFromStore;

    public NearestDrivers(String driverID, double latitude, double longitude, double distanceFromStore) {
        this.driverID = driverID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceFromStore = distanceFromStore;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistanceFromStore() {
        return distanceFromStore;
    }

    public void setDistanceFromStore(double distanceFromStore) {
        this.distanceFromStore = distanceFromStore;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"driverID\":\"").append(getDriverID()).append("\"")
                .append(",\"latitude\":").append(getLatitude())
                .append(",\"longitude\":").append(getLongitude())
                .append(",\"distanceFromStore\":").append(getDistanceFromStore());
        return builder.append("}").toString();
    }
}
