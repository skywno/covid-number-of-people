package com.example.covid.controller;


import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.LocationDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/locations")
    public ModelAndView adminPlaces(
            LocationType locationType, String locationName, String address
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put("locationType", locationType);
        model.put("locationName", locationName);
        model.put("address", address);

        return new ModelAndView("admin/locations", model);
    }

    @GetMapping("/locations/{locationId}")
    public ModelAndView adminPlaceDetail(@PathVariable Integer locationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("location", LocationDto.of(
                LocationType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
        return new ModelAndView("admin/location-detail", map);    }

    @GetMapping("/events")
    public ModelAndView adminEvents(
            Long locationId,
            String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime
                    eventStartDatetime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime
                    eventEndDatetime
            ) {
        Map<String, Object> map = new HashMap<>();
        map.put("locationName", "location-" + locationId);
        map.put("eventName", eventName);
        map.put("eventStatus", eventStatus);
        map.put("eventStartDatetime", eventStartDatetime);
        map.put("eventEndDatetime", eventEndDatetime);
        return new ModelAndView("admin/events", map);    }

    @GetMapping("/events/{eventId}")
    public ModelAndView adminEventDetail(@PathVariable Long eventId) {
        Map<String, Object> map = new HashMap<>();
        map.put("event", EventDto.of(
                eventId,
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021,1,1,13,0,0),
                LocalDateTime.of(2021,1,1,16,0,0),
                0,
                24,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
        return new ModelAndView("admin/event-detail", map);
    }


}
