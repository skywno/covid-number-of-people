package com.example.covid.controller.api;


import com.example.covid.constant.EventStatus;
import com.example.covid.dto.APIDataResponse;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventRequest;
import com.example.covid.dto.EventResponse;
import com.example.covid.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


//@Validated
@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/events")
public class APIEventController {

    private final EventService eventService;
    @GetMapping
    public APIDataResponse<List<EventResponse>> getEvents(
            @Positive Long locationId,
            @Size(min = 2) @NotBlank String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDateTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDateTime
    ) {
        List<EventDto> response = eventService.getEvents(locationId, eventName,
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
            @Valid @RequestBody EventRequest eventRequest
    ) {
        Boolean result = eventService.createEvent(eventRequest.toDTO());
        return APIDataResponse.of(result.toString());
    }

    @GetMapping("/{eventId}")
    public APIDataResponse<EventResponse> getEvent(
            @Positive @PathVariable Long eventId
    ) {
        EventResponse eventResponse = EventResponse.from(
                eventService.getEvent(eventId).orElse(null)
        );

        return APIDataResponse.of(eventResponse);
    }

    @PutMapping("/{eventId}")
    public APIDataResponse<String> modifyEvent(
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody EventRequest eventRequest
    ) {
        Boolean result = eventService.modifyEvent(eventId, eventRequest.toDTO());
        return APIDataResponse.of(result.toString());
    }

    @DeleteMapping("/{eventId}")
    public APIDataResponse<String> deleteEvent(@Positive @PathVariable Long eventId){
        Boolean result = eventService.removeEvent(eventId);
        return APIDataResponse.of(result.toString());
    }

}
