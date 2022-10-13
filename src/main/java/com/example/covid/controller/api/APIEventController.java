package com.example.covid.controller.api;


import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.dto.*;
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
            @NotBlank String locationName,
            @Size(min = 2) @NotBlank String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDateTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDateTime
    ) {
        // TODO: 임시 데이터. 추후 삭제 예정
        return APIDataResponse.of(List.of(EventResponse.of(
                1L,
                LocationDto.of(
                        1L,
                        LocationType.SPORTS,
                        "배드민턴장",
                        "서울시 가나구 다라동",
                        "010-1111-2222",
                        0,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        )));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public APIDataResponse<String> createEvent(
            @Valid @RequestBody EventRequest eventRequest
    ) {
        Boolean result = eventService.createEvent(eventRequest.toDto());
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
        Boolean result = eventService.modifyEvent(eventId, eventRequest.toDto());
        return APIDataResponse.of(result.toString());
    }

    @DeleteMapping("/{eventId}")
    public APIDataResponse<String> deleteEvent(@Positive @PathVariable Long eventId){
        Boolean result = eventService.removeEvent(eventId);
        return APIDataResponse.of(result.toString());
    }

}
