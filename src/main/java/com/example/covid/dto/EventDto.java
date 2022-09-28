package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import com.example.covid.domain.Event;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        Long locationId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,
        Integer currentNumberOfPeople,
        Integer capacity,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static EventDto of (
            Long id, Long locationId, String eventName, EventStatus eventStatus,
            LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople, Integer capacity,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        return new EventDto(
                id,
                locationId,
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
                event.getLocationId(),
                event.getName(),
                event.getStatus(),
                event.getEventStartDateTime(),
                event.getEventEndDateTime(),
                event.getCurrentNumberOfPeople(),
                event.getCapacity(),
                event.getCreatedAt(),
                event.getModifiedAt()
        );
    }

    public Event toEntity() {
        return Event.of(
                eventName,
                locationId,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity
        );
    }

    public Event updateEntity(Event event) {
        if (locationId != null) { event.setLocationId(locationId); }
        if (eventName != null) { event.setName(eventName); }
        if (eventStatus != null) { event.setStatus(eventStatus); }
        if (eventStartDateTime != null) { event.setEventStartDateTime(eventStartDateTime); }
        if (eventEndDateTime != null) { event.setEventEndDateTime(eventEndDateTime); }
        if (currentNumberOfPeople != null) { event.setCurrentNumberOfPeople(currentNumberOfPeople); }
        if (capacity != null) { event.setCapacity(capacity); }

        return event;
    }
}
