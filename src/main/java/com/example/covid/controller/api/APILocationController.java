package com.example.covid.controller.api;


import com.example.covid.constant.LocationType;
import com.example.covid.dto.APIDataResponse;
import com.example.covid.dto.LocationRequest;
import com.example.covid.dto.LocationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/locations")
@RestController
public class APILocationController {

    @GetMapping
    public APIDataResponse<List<LocationResponse>> getLocations() {
        return APIDataResponse.of(List.of(LocationResponse.of(
                LocationType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30
        )));

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public APIDataResponse<Void> createLocation(
            @RequestBody LocationRequest locationRequest
    ) {
        return APIDataResponse.empty();
    }

    @GetMapping("/{locationId}")
    public APIDataResponse<LocationResponse> getLocation(@PathVariable Long locationId) {
        if (locationId.equals(2L)) {
            return APIDataResponse.empty();
        }

        return APIDataResponse.of(LocationResponse.of(
                LocationType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30
        ));
    }

    @PutMapping("/{locationId}")
    public APIDataResponse<Void> modifyLocation(
            @PathVariable Long locationId,
            @RequestBody LocationRequest locationRequest
    ) {
        return APIDataResponse.empty();
    }

    @DeleteMapping("/{locationId}")
    public APIDataResponse<Void> removeLocation(@PathVariable Long locationId) {
        return APIDataResponse.empty();
    }
}
