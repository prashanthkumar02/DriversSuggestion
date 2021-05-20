package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "store")
@ApiModel(description = "Store object sample")
public class Store {

    @Id
    @Column(name = "store_id")
    String storeID;

    @Column(name = "store_latitude")
    @NotNull
    double latitude;

    @Column(name = "store_longitude")
    @NotNull
    double longitude;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"storeID\":\"").append(getStoreID()).append("\"")
                .append(",\"latitude\":").append(getLatitude())
                .append(",\"longitude\":").append(getLongitude());
        return builder.append("}").toString();
    }
}
