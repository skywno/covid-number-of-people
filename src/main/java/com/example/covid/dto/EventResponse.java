package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        Long locationId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity
) {
    public static EventResponse of(
            Long id,
            Long locationId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventResponse(
                id,
                locationId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity
        );
    }

    public static EventResponse from(EventDto eventDto) {
        if (eventDto == null) {return null;}

        return EventResponse.of(
                eventDto.id(),  
                eventDto.locationId(),
                eventDto.eventName(),
                eventDto.eventStatus(),
                eventDto.eventStartDateTime(),
                eventDto.eventEndDateTime(),
                eventDto.currentNumberOfPeople(),
                eventDto.capacity()
        );
    }
}
