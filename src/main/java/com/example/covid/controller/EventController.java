package com.example.covid.controller;

import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Event;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventResponse;
import com.example.covid.dto.EventViewResponse;
import com.example.covid.dto.LocationDto;
import com.example.covid.exception.GeneralException;
import com.example.covid.service.EventService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Validated
@RequestMapping("/events")
@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    @GetMapping
    public ModelAndView events(@QuerydslPredicate(root = Event.class) Predicate predicate) {
        Map<String, Object> map = new HashMap<>();

        List<EventResponse> events = eventService.getEvents(predicate)
                .stream()
                .map(EventResponse::from)
                .toList();

        map.put("events", events);

        return new ModelAndView("event/index", map);
    }

    @GetMapping("/custom")
    public ModelAndView customEvents(
            @Size(min = 2) String locationName,
            @Size(min = 2) String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDatetime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDatetime,
            Pageable pageable
    ) {
        log.info("{} {} {} {}", locationName, eventName, eventStatus, pageable);
        Page<EventViewResponse> response = eventService.getEventViewResponse(
                locationName, eventName, eventStatus,
                eventStartDatetime, eventEndDatetime, pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("events", response);

        return new ModelAndView("event/index", map);


    }

    @GetMapping("/{eventId}")
    public ModelAndView eventDetail(@PathVariable Long eventId) {
        Map<String, Object> map = new HashMap<>();

        EventResponse response =
                eventService.getEvent(eventId).map(EventResponse::from)
                        .orElseThrow(() -> new GeneralException(ErrorCode.DATA_ACCESS_ERROR));

        map.put("event", response);
        return new ModelAndView("event/detail", map);
    }

}
