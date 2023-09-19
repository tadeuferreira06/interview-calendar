package com.tamanna.challenge.interview.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * @author tlferreira
 */
@Builder
@Data
public class BaseResponse <T> {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;
}
