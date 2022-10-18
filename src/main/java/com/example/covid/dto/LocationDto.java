package com.example.covid.dto;

import com.example.covid.constant.LocationType;
import com.example.covid.domain.Location;

import java.time.LocalDateTime;

public record LocationDto(
        Long id,
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static LocationDto of(
            Long id,
        LocationType locationType,
        String locationName,
        String address,
        String phoneNumber,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    return new LocationDto(id, locationType, locationName, address, phoneNumber, capacity, createdAt, modifiedAt);
}


    public static LocationDto of(Location location) {
        return new LocationDto(
                location.getId(),
                location.getLocationType(),
                location.getLocationName(),
                location.getAddress(),
                location.getPhoneNumber(),
                location.getCapacity(),
                location.getCreatedAt(),
                location.getModifiedAt()
        );
    }

    public static LocationDto idOnly(Long id) {
        return LocationDto.of(id, null, null, null, null, null, null, null);
    }

    public Location toEntity() {
        return Location.of(locationType, locationName, address, phoneNumber, capacity);
    }

    public Location updateEntity(Location location) {
        if (locationType != null) {
            location.setLocationType(locationType);
        }
        if (locationName != null) {
            location.setLocationName(locationName);
        }
        if (address != null) {
            location.setAddress(address);
        }
        if (phoneNumber != null) {
            location.setPhoneNumber(phoneNumber);
        }
        if (capacity != null) {
            location.setCapacity(capacity);
        }
        return location;
    }
}
