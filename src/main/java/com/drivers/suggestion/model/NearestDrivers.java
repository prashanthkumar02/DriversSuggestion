package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "Return driver details example")
@Getter
@Setter
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
