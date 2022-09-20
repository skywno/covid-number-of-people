package com.example.covid.dto;

import com.example.covid.constant.LocationType;

public record LocationResponse(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity
) {
    public static LocationResponse of(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new LocationResponse(locationType, locationName, address, phoneNumber, capacity);
    }
}

