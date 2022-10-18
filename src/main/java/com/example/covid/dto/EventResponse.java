package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        LocationDto location,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity
) {
    public static EventResponse of(
            Long id,
            LocationDto location,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventResponse(
                id,
                location,
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
                eventDto.locationDto(),
                eventDto.eventName(),
                eventDto.eventStatus(),
                eventDto.eventStartDateTime(),
                eventDto.eventEndDateTime(),
                eventDto.currentNumberOfPeople(),
                eventDto.capacity()
        );
    }

    public static EventResponse empty(LocationDto locationDto) {
        return EventResponse.of(null, locationDto, null, null, null, null, null, null);
    }

    public String getLocationName() {
        return this.location().locationName();
    }
}
