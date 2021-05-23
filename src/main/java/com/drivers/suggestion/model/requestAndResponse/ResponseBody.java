package com.drivers.suggestion.model.requestAndResponse;

import com.drivers.suggestion.model.NearestDrivers;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody {

    private Object message;
    private String status;
    private List<NearestDrivers> driversFetched;

}
