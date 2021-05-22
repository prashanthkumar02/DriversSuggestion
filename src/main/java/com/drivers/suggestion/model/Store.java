package com.drivers.suggestion.model;

import com.drivers.suggestion.model.validator.constraints.IsLatitudeInFormat;
import com.drivers.suggestion.model.validator.constraints.IsLongitudeInFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity
@DynamicUpdate
@Getter
@Setter
@Valid
@EqualsAndHashCode
@ToString
@Table(name = "store")
@ApiModel(description = "Store object sample")
public class Store implements Persistable<String> {

    @Id
    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "store_id")
    @Pattern(regexp = "^[a-zA-Z0-9@\\. _-]*$", message = "One or more record(s) have invalid format, accepted characters a-zA-Z0-9@._-")
    private String storeID;

    @Column(name = "store_latitude")
    @IsLatitudeInFormat
    private Double latitude = -999.00;

    @Column(name = "store_longitude")
    @IsLongitudeInFormat
    private Double longitude = -999.00;

    @Transient
    @ApiModelProperty(hidden = true)
    private boolean oldEntry;

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
}
