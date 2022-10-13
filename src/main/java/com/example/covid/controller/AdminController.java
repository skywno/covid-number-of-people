package com.example.covid.controller;


import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Event;
import com.example.covid.domain.Location;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventResponse;
import com.example.covid.dto.LocationDto;
import com.example.covid.dto.LocationResponse;
import com.example.covid.exception.GeneralException;
import com.example.covid.service.EventService;
import com.example.covid.service.LocationService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final EventService eventService;
    private final LocationService locationService;

    @GetMapping("/locations")
    public ModelAndView adminLocations(
            @QuerydslPredicate(root = Location.class) Predicate predicate
    ) {

        List<LocationResponse> locations = locationService.getLocations(predicate)
                .stream()
                .map(LocationResponse::from)
                .toList();

        return new ModelAndView("admin/locations", Map.of(
                "locations", locations,
                "locationType", LocationType.values()
        ));
    }

    @GetMapping("/locations/{locationId}")
    public ModelAndView adminLocationDetail(@PathVariable Long locationId) {

        LocationResponse location = locationService.getLocation(locationId)
                .map(LocationResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));


        return new ModelAndView("admin/location-detail", Map.of(
                "location", location,
                "locationType", LocationType.values()
        ));
    }

    @GetMapping("/events")
    public ModelAndView adminEvents(
            @QuerydslPredicate(root = Event.class) Predicate predicate
    ) {

        List<EventResponse> responses = eventService.getEvents(predicate)
                .stream().map(EventResponse::from)
                .toList();

        return new ModelAndView("admin/events", Map.of(
                "events", responses,
                "eventStatus", EventStatus.values()
        ));
    }

    @GetMapping("/events/{eventId}")
    public ModelAndView adminEventDetail(@PathVariable Long eventId) {

        EventResponse eventResponse = eventService.getEvent(eventId)
                .map(EventResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        return new ModelAndView("admin/event-detail", Map.of(
                "event", eventResponse,
                "eventStatus", EventStatus.values()
        ));
    }


}
