package com.example.covid.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdminOperationStatus {
    CREATE("생성"),
    UPDATE("수정"),
    DELETE("삭제");

    private final String message;
}
