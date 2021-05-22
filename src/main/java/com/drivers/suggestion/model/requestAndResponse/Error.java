package com.drivers.suggestion.model.requestAndResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Error {
    String fieldName;
    String errorMessage;
}
