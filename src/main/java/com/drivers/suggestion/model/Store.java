package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Store object sample")
public class Store {

    int storeId;
    double storeLatitude;
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
        return "Store{" +
                "storeId=" + storeId +
                ", storeLatitude=" + storeLatitude +
                ", storeLongitude=" + storeLongitude +
                '}';
    }
}
