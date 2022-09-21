package com.example.covid.controller.api;


import com.example.covid.constant.EventStatus;
import com.example.covid.dto.APIDataResponse;
import com.example.covid.dto.EventDTO;
import com.example.covid.dto.EventRequest;
import com.example.covid.dto.EventResponse;
import com.example.covid.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class APIEventController {

    private final EventService eventService;
    @GetMapping
    public APIDataResponse<List<EventResponse>> getEvents(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDateTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDateTime
    ) {
        List<EventDTO> response = eventService.getEvents(locationId, eventName,
                eventStatus, eventStartDateTime, eventEndDateTime);

        return APIDataResponse.of(
                response.stream()
                        .map(EventResponse::from)
                        .toList()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public APIDataResponse<String> createEvent(
            @RequestBody EventRequest eventRequest
    ) {
        Boolean result = eventService.createEvent(eventRequest.toDTO());
        return APIDataResponse.of(result.toString());
    }

    @GetMapping("/{eventId}")
    public APIDataResponse<EventResponse> getEvent(@PathVariable Long eventId) {
        EventResponse eventResponse = EventResponse.from(
                eventService.getEvent(eventId).orElse(null)
        );

        return APIDataResponse.of(eventResponse);
    }

    @PutMapping("/{eventId}")
    public APIDataResponse<String> modifyEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest
    ) {
        Boolean result = eventService.modifyEvent(eventId, eventRequest.toDTO());
        return APIDataResponse.of(result.toString());
    }

    @DeleteMapping("/{eventId}")
    public APIDataResponse<String> deleteEvent(@PathVariable Long eventId){
        Boolean result = eventService.removeEvent(eventId);
        return APIDataResponse.of(result.toString());
    }

}
