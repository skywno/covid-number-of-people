package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import java.time.LocalDateTime;

public record EventResponse(
        Long locationId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity
) {
    public static EventResponse of(
            Long locationId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventResponse(
                locationId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity
        );
    }
}
