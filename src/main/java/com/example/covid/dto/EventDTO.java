package com.example.covid.dto;

import com.example.covid.constant.EventStatus;

import java.time.LocalDateTime;

public record EventDTO(
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

    public static EventDTO of (
            Long id, Long locationId, String eventName, EventStatus eventStatus,
            LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime,
            Integer currentNumberOfPeople, Integer capacity,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        return new EventDTO(
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
}
