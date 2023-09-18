package com.tamanna.challenge.interview.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * @author tlferreira
 */

@Builder
@Data
public class ApiError {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parameter;
    private String message;
}
