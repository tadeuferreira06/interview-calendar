package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author tlferreira
 */
@ControllerAdvice(assignableTypes = {PersonController.class, PersonScheduleController.class})
@Component
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException exception) {
        return validationErrorBuilder(exception, "Missing Parameter: " + exception.getParameter());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return validationErrorBuilder(exception, "Missing Parameter: " + exception.getParameterName());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        return validationErrorBuilder(exception, "Missing Header: " + exception.getHeaderName());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        String errors = exception
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return validationErrorBuilder(exception, errors);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException exception) {
        String errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        return validationErrorBuilder(exception, errors);
    }

    private ResponseEntity<String> validationErrorBuilder(Exception exception, String errors) {
        String errorMessage = "Error while validating method params: " + exception.getMessage();
        log.error(errorMessage);
        String message = "Validation Error";
        if (StringUtils.hasText(errors)) {
            message += ": " + errors;
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleException(ServiceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleException(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleException(Exception exception) {
        log.error("Internal Server Error, Exception: ", exception);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
