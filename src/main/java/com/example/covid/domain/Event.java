package com.example.covid.domain;

import com.example.covid.constant.EventStatus;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Event {
    private BigInteger id;
    private String name;
    private EventStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer currentNumberOfPeople;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
