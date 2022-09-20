package com.example.covid.controller.api;


import com.example.covid.constant.EventStatus;
import com.example.covid.dto.APIDataResponse;
import com.example.covid.dto.EventRequest;
import com.example.covid.dto.EventResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/events")
public class APIEventController {

    @GetMapping
    public APIDataResponse<List<EventResponse>> getEvents() {
        return APIDataResponse.of(List.of(EventResponse.of(
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021,1,1,13,0,0),
                LocalDateTime.of(2021,1,1,16,0,0),
                0,
                24

        )));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public APIDataResponse<EventResponse> createEvent(
            @RequestBody EventRequest eventRequest
    ) {
        return APIDataResponse.empty();
    }

    @GetMapping("/{eventId}")
    public APIDataResponse<EventResponse> getEvent(@PathVariable Long eventId) {
        if (eventId.equals(2L)) {
            return APIDataResponse.empty();
        }

        return APIDataResponse.of(EventResponse.of(
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        ));
    }

    @PutMapping("/{eventId}")
    public APIDataResponse<Void> modifyEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest
    ) {
        return APIDataResponse.empty();
    }

    @DeleteMapping("/{eventId}")
    public APIDataResponse<Void> deleteEvent(@PathVariable Long eventId){
        return APIDataResponse.empty();
    }
}
