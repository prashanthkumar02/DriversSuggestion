package com.drivers.suggestion.model;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "driver")
@DynamicUpdate
@SqlResultSetMapping(
    name = "nearestDriversMapping",
    classes = {
        @ConstructorResult(
            targetClass = com.drivers.suggestion.model.NearestDrivers.class,
            columns = {
                @ColumnResult( name = "driver_id", type = String.class),
                @ColumnResult( name = "driver_latitude", type = Double.class),
                @ColumnResult( name = "driver_longitude", type = Double.class),
                @ColumnResult( name = "distanceFromStore", type = Double.class)
            }
        )
    }
)
@NamedNativeQuery(
        name = "nearestDriversMapping",
        resultClass = NearestDrivers.class,
        resultSetMapping ="nearestDriversMapping",
        query = "SELECT d.driver_id, d.driver_latitude, d.driver_longitude, ROUND(SQRT(POWER((d.driver_latitude - s.store_latitude), 2) + POWER((d.driver_longitude - s.store_longitude),2)), 3) AS distanceFromStore FROM store AS s JOIN driver d WHERE s.store_id = ?1 ORDER BY distanceFromStore ASC LIMIT ?2")
@ApiModel(description = "Driver object sample")
public class Driver {

    @Id
    @Column(name = "driver_id")
    @Pattern(regexp = "^[a-zA-Z0-9@\\. _-]*$")
    String driverID;

    @Column(name = "driver_latitude")
    double latitude = -999;

    @Column(name = "driver_longitude")
    double longitude = -999;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"driverID\":\"").append(getDriverID()).append("\"")
                .append(",\"latitude\":").append(getLatitude())
                .append(",\"longitude\":").append(getLongitude());
        return builder.append("}").toString();
    }
}
