package com.example.covid.dto;


public record AdminRequest(
        String email,
        String nickname,
        String password,
        String phoneNumber
) {
    public static AdminRequest of (
            String email,
            String nickname,
            String password,
            String phoneNumber
    ) {
        return new AdminRequest(email, nickname, password, phoneNumber);
    }
}
