package com.drivers.suggestion.model;

import com.drivers.suggestion.model.validator.constraints.IsLatitudeInFormat;
import com.drivers.suggestion.model.validator.constraints.IsLongitudeInFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@DynamicUpdate
@Getter
@Setter
@ToString
@Table(name = "driver")
@ApiModel(description = "Driver object sample")
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
public class Driver {

    @Id
    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "driver_id")
    @Pattern(regexp = "^[a-zA-Z0-9@\\. _-]*$", message = "One or more record(s) have invalid format, accepted characters a-zA-Z0-9@._-")
    private String driverID;

    @Column(name = "driver_latitude")
    @IsLatitudeInFormat
    private Double latitude = -999.00;

    @Column(name = "driver_longitude")
    @IsLongitudeInFormat
    private Double longitude = -999.00;
}
