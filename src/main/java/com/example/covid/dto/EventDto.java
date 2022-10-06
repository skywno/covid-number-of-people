package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import com.example.covid.domain.Event;
import com.example.covid.domain.Location;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        LocationDto locationDto,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static EventDto of(
            Long id, LocationDto locationDto, String eventName, EventStatus eventStatus,
            LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople, Integer capacity,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        return new EventDto(
                id,
                locationDto,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity,
                createdAt,
                modifiedAt
        );
    }

    public static EventDto of(Event event) {
        return new EventDto(
                event.getId(),
                LocationDto.of(event.getLocation()),
                event.getEventName(),
                event.getEventStatus(),
                event.getEventStartDateTime(),
                event.getEventEndDateTime(),
                event.getCurrentNumberOfPeople(),
                event.getCapacity(),
                event.getCreatedAt(),
                event.getModifiedAt()
        );
    }

    public Event toEntity(Location location) {
        return Event.of(
                eventName,
                location,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity
        );
    }

    public Event updateEntity(Event event) {
        if (eventName != null) {
            event.setEventName(eventName);
        }
        if (eventStatus != null) {
            event.setEventStatus(eventStatus);
        }
        if (eventStartDateTime != null) {
            event.setEventStartDateTime(eventStartDateTime);
        }
        if (eventEndDateTime != null) {
            event.setEventEndDateTime(eventEndDateTime);
        }
        if (currentNumberOfPeople != null) {
            event.setCurrentNumberOfPeople(currentNumberOfPeople);
        }
        if (capacity != null) {
            event.setCapacity(capacity);
        }

        return event;
    }
}
