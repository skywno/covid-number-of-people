package com.example.covid.dto;

import com.example.covid.constant.LocationType;

import java.time.LocalDateTime;

public record LocationDto(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static LocationDto of(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    return new LocationDto(locationType, locationName, address, phoneNumber, capacity, createdAt, modifiedAt);
}
}
