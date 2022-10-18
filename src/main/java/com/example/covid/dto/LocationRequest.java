package com.example.covid.dto;

import com.example.covid.constant.LocationType;

public record LocationRequest(
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity
) {
    public static LocationRequest of(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new LocationRequest(locationType, locationName, address, phoneNumber, capacity);
    }

    public LocationDto toDto() {
        return LocationDto.of(
                null,
                this.locationType(),
                this.locationName(),
                this.address(),
                this.phoneNumber(),
                this.capacity(),
                null,
                null
        );
    }
}
