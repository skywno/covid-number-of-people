package com.example.covid.controller;

import com.example.covid.constant.ErrorCode;
import com.example.covid.domain.Location;
import com.example.covid.dto.LocationResponse;
import com.example.covid.exception.GeneralException;
import com.example.covid.service.LocationService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/locations")
@RequiredArgsConstructor
@Controller
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ModelAndView getLocations(@QuerydslPredicate(root = Location.class) Predicate predicate) {
        Map<String, Object> map = new HashMap<>();
        List<LocationResponse> responses = locationService.getLocations(predicate)
                .stream()
                .map(LocationResponse::from)
                .toList();
        map.put("locations", responses);

        return new ModelAndView("location/index", map);
    }

    @GetMapping("/{locationId}")
    public ModelAndView getLocation(@PathVariable Long locationId) {
        Map<String, Object> map = new HashMap<>();

        LocationResponse location = locationService.getLocation(locationId)
                .map(LocationResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));
        map.put("location", location);

        return new ModelAndView("location/detail", map);
    }
}
