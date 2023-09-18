package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author tlferreira
 */
public class ControllerUtils {
    private static final String DEFAULT_MESSAGE = "Ok";

    private ControllerUtils() {
        //private
    }

    public static <T> ResponseEntity<BaseResponse<T>> buildResponse(HttpStatus status) {
        return buildResponse(status, status.getReasonPhrase());
    }

    public static <T> ResponseEntity<BaseResponse<T>> buildResponse(T responseObject, HttpStatus status) {
        return buildResponse(status, DEFAULT_MESSAGE, responseObject);
    }

    public static <T> ResponseEntity<BaseResponse<T>> buildResponse(HttpStatus status, String message) {
        return buildResponse(status, message, null);
    }

    public static <T> ResponseEntity<BaseResponse<T>> buildResponse(HttpStatus status, String message, T responseObject) {
        return new ResponseEntity<>(BaseResponse
                .<T>builder()
                .status(status.value())
                .message(message)
                .response(responseObject)
                .build(),
                status);
    }

}
