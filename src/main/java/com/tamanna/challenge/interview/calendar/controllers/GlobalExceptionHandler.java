package com.tamanna.challenge.interview.calendar.controllers;

import com.tamanna.challenge.interview.calendar.dtos.ApiError;
import com.tamanna.challenge.interview.calendar.dtos.BaseResponse;
import com.tamanna.challenge.interview.calendar.exceptions.NotFoundException;
import com.tamanna.challenge.interview.calendar.exceptions.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.tamanna.challenge.interview.calendar.controllers.ControllerUtils.buildResponse;

/**
 * @author tlferreira
 */
@ControllerAdvice(assignableTypes = {InterviewerController.class, InterviewerScheduleController.class, CandidateController.class, CandidateScheduleController.class, MeetingController.class})
@Component
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ApiError>>> handleMissingPathVariableException(MissingPathVariableException exception) {
        return validationErrorBuilder(exception, List.of(ApiError.builder().parameter(exception.getParameter().toString()).message("Missing Parameter").build()));
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ApiError>>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return validationErrorBuilder(exception, List.of(ApiError.builder().parameter(exception.getParameterName()).message("Missing Parameter").build()));
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ApiError>>> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        return validationErrorBuilder(exception, List.of(ApiError.builder().parameter(exception.getHeaderName()).message("Missing Header").build()));
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ApiError>>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ApiError> errors = exception
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .map(message -> ApiError.builder().parameter("Constraint").message(message).build())
                .toList();
        return validationErrorBuilder(exception, errors);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ApiError>>> handleConstraintViolationException(MethodArgumentNotValidException exception) {
        List<ApiError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ApiError.builder().parameter(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
                .toList();
        return validationErrorBuilder(exception, errors);
    }

    private ResponseEntity<BaseResponse<List<ApiError>>> validationErrorBuilder(Exception exception, List<ApiError> errors) {
        String errorMessage = "Error while validating method params: " + exception.getMessage();
        log.error(errorMessage);
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, errors);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<BaseResponse<T>> handleNotFoundException(NotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<BaseResponse<T>> handleServiceException(ServiceException exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<BaseResponse<T>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<BaseResponse<T>> handleException(Exception exception) {
        log.error("Internal Server Error, Exception: ", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

}
