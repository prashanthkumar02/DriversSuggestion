package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "store")
@ApiModel(description = "Store object sample")
public class Store implements Persistable<String> {

    @Id
    @Column(name = "store_id")
    @Pattern(regexp = "^[a-zA-Z0-9@\\. _-]*$")
    String storeID;

    @Transient
    @ApiModelProperty(hidden = true)
    private boolean oldEntry;

    @Column(name = "store_latitude")
    double latitude = -999;

    @Column(name = "store_longitude")
    double longitude = -999;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public boolean isOldEntry() {
        return oldEntry;
    }

    public void setOldEntry(boolean oldEntry) {
        this.oldEntry = oldEntry;
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
    public String getId() {
        return storeID;
    }

    @Override
    public boolean isNew() {
        return !this.oldEntry;
    }

    @PrePersist
    @PostLoad
    void markUpdated() {
        this.oldEntry = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Double.compare(store.latitude, latitude) == 0 && Double.compare(store.longitude, longitude) == 0 && Objects.equals(storeID, store.storeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeID, latitude, longitude);
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
