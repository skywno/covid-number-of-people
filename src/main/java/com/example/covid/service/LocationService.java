package com.example.covid.service;


import com.example.covid.dto.LocationDto;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    public List<LocationDto> getLocations(Predicate predicate) {
        return null;
    }

    public Optional<LocationDto> getLocation(Long locationId) {
        return null;
    }
}
