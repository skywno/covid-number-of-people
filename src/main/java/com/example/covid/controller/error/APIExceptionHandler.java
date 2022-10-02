package com.example.covid.controller.error;


import com.example.covid.constant.ErrorCode;
import com.example.covid.dto.APIErrorResponse;
import com.example.covid.exception.GeneralException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, RepositoryRestController.class})
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> constraintException(
            ConstraintViolationException e,
            WebRequest request
    ) {
        return getSuperHandleExceptionInternal(
                e,
                ErrorCode.VALIDATION_ERROR,
                HttpHeaders.EMPTY,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        ErrorCode errorCode = e.getErrorCode();

        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode, errorCode.getMessage(e)),
                HttpHeaders.EMPTY,
                errorCode.getHttpStatus(),
                request
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {

        return getSuperHandleExceptionInternal(
                e,
                ErrorCode.INTERNAL_ERROR,
                HttpHeaders.EMPTY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception e,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorCode errorCode = status.is4xxClientError() ?
                ErrorCode.SPRING_BAD_REQUEST :
                ErrorCode.SPRING_INTERNAL_ERROR;

        return getSuperHandleExceptionInternal(e, errorCode, headers, status, request);
    }


    private ResponseEntity<Object> getSuperHandleExceptionInternal(
            Exception e,
            ErrorCode errorCode,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode, errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}
