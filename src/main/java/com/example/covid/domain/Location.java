package com.example.covid.domain;

import com.example.covid.constant.LocationType;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Location {
    private BigInteger id;
    private String name;
    private LocationType type;
    private String address;
    private String phoneNumber;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
