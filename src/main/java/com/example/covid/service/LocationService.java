package com.example.covid.service;


import com.example.covid.constant.ErrorCode;
import com.example.covid.domain.Location;
import com.example.covid.dto.LocationDto;
import com.example.covid.exception.GeneralException;
import com.example.covid.repository.LocationRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> getLocations(Predicate predicate) {
        try {
            return StreamSupport
                    .stream(locationRepository.findAll(predicate).spliterator(), false)
                    .map(LocationDto::of)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR);
        }
    }

    public Optional<LocationDto> getLocation(Long locationId) {
        try {
            return locationRepository.findById(locationId)
                    .map(LocationDto::of);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR);
        }
    }

    public boolean createLocation(LocationDto locationDto) {
        try {
            if (locationDto == null) {return false;}

            locationRepository.save(locationDto.toEntity());
            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean modifyLocation(Long locationId, LocationDto dto) {
        try {
            if (locationId == null || dto == null) {
                return false;
            }

            locationRepository.findById(locationId)
                    .ifPresent(location -> locationRepository.save(dto.updateEntity(location)));

            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean removeLocation(Long locationId) {
        try {
            if (locationId == null) return false;

            Optional<Location> result = locationRepository.findById(locationId);
            if (result.isPresent()) {
                locationRepository.deleteById(locationId);
                return true;
            }
            return false;

        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }

    }
}
