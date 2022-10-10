package com.example.covid.dto;

import com.example.covid.constant.EventStatus;

import java.time.LocalDateTime;

public record EventViewResponse(
        Long id,
        String locationName,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity
) {
    public EventViewResponse(
            Long id,
            String locationName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        this.id = id;
        this.locationName = locationName;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
    }

    public static EventViewResponse of(
            Long id,
            String locationName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventViewResponse(id, locationName, eventName, eventStatus,
                eventStartDateTime, eventEndDateTime, currentNumberOfPeople, capacity);
    }

    public static EventViewResponse from(EventDto eventDTO) {
        if (eventDTO == null) { return null; }
        return EventViewResponse.of(
                eventDTO.id(),
                eventDTO.locationDto().locationName(),
                eventDTO.eventName(),
                eventDTO.eventStatus(),
                eventDTO.eventStartDateTime(),
                eventDTO.eventEndDateTime(),
                eventDTO.currentNumberOfPeople(),
                eventDTO.capacity()
        );
    }
}
