package com.example.covid.dto;

import com.example.covid.constant.LocationType;

public record LocationResponse(
        Long id,
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity
) {
    public static LocationResponse of(
            Long id,
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new LocationResponse(id, locationType, locationName, address, phoneNumber, capacity);
    }

    public static LocationResponse from (LocationDto dto) {
        return LocationResponse.of(
                dto.id(),
                dto.locationType(),
                dto.locationName(),
                dto.address(),
                dto.phoneNumber(),
                dto.capacity()
        );
    }
}

