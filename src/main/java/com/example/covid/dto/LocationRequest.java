package com.example.covid.dto;

import com.example.covid.constant.LocationType;

public record LocationRequest(
        Long id,
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity
) {
    public static LocationRequest of(
            Long id,
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new LocationRequest(id, locationType, locationName, address, phoneNumber, capacity);
    }

    public LocationDto toDto() {
        return LocationDto.of(
                this.id(),
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
