package com.example.covid.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Admin {
    private BigInteger id;
    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
