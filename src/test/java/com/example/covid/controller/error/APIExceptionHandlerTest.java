package com.example.covid.controller.error;

import com.example.covid.constant.ErrorCode;
import com.example.covid.dto.APIErrorResponse;
import com.example.covid.exception.GeneralException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.validation.ConstraintViolationException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class APIExceptionHandlerTest {

    private APIExceptionHandler sut;
    private WebRequest request;

    @BeforeEach
    void setUp() {
        this.sut = new APIExceptionHandler();
        this.request = new DispatcherServletWebRequest(new MockHttpServletRequest());
    }

    @DisplayName("검증 오류")
    @Test
    void givenValidationException_whenCallingValidation_thenResponseEntity () {
        // Given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());

        // When
        ResponseEntity<Object> response = sut.constraintException(e, request);
        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", APIErrorResponse.of(false, ErrorCode.VALIDATION_ERROR, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);

    }

    @DisplayName("프로젝트 일반 오류")
    @Test
    void givenGeneralException_returnsResponseEntity() {
        // Given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        GeneralException e = new GeneralException(errorCode);
        // When
        ResponseEntity<Object> response = sut.general(e, request);
        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", APIErrorResponse.of(false, errorCode, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @DisplayName("기타 오류")
    @Test
    void givenAnyOtherException_returnsResponseEntity() {
        // Given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        Exception e = new Exception("This is a test");
        // When
        ResponseEntity<Object> response = sut.exception(e, request);
        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", APIErrorResponse.of(false, ErrorCode.INTERNAL_ERROR, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}