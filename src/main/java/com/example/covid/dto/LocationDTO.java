package com.example.covid.dto;

import com.example.covid.constant.LocationType;

import java.time.LocalDateTime;

public record LocationDTO(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        String memo,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static LocationDTO of(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        String memo,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    return new LocationDTO(locationType, locationName, address, phoneNumber, capacity, memo, createdAt, modifiedAt);
}
}
