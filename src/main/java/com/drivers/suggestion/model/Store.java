package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store")
@ApiModel(description = "Store object sample")
public class Store {

    @Id
    @Column(name = "store_id")
    int storeId;

    @Column(name = "store_latitude")
    double storeLatitude;

    @Column(name = "store_longitude")
    double storeLongitude;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(double storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public double getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(double storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"storeId\":\"").append(getStoreId()).append("\"")
                .append(",\"storeLatitude\":").append(getStoreLatitude())
                .append(",\"storeLongitude\":").append(getStoreLongitude());
        return builder.append("}").toString();
    }
}
