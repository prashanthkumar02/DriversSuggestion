package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "driver")
@ApiModel(description = "Driver object sample")
public class Driver {

    @Id
    @Column(name = "driver_id")
    String driverId;

    @Column(name = "driver_latitude")
    double driversLatitude;

    @Column(name = "driver_longitude")
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"driverId\":\"").append(getDriverId()).append("\"")
                .append(",\"driverLatitude\":").append(getDriversLatitude())
                .append(",\"driverLongitude\":").append(getDriversLongitude());
        return builder.append("}").toString();
    }
}
