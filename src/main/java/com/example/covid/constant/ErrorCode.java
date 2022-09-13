package com.example.covid.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Predicate;

import static com.example.covid.constant.ErrorCode.ErrorCategory.*;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, NORMAL, "Ok"),
    BAD_REQUEST(10000, CLIENT_SIDE, "Bad request"),
    SPRING_BAD_REQUEST(10001, CLIENT_SIDE, "Spring-detected bad request"),
    INTERNAL_ERROR(20000, SERVER_SIDE, "Internal error"),
    SPRING_INTERNAL_ERROR(20001, SERVER_SIDE, "Spring-detected internal error");

    private final Integer code;
    private final ErrorCategory errorCategory;
    private final String message;

    public String getMessage(Exception e) {
        return getMessage(e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(getMessage());
    }

    @Override
    public String toString() {return String.format("%s {%d}", name(), this.getCode());}

    public enum ErrorCategory {
        NORMAL, CLIENT_SIDE, SERVER_SIDE
    }
}


