package com.example.covid.dto;

import com.example.covid.constant.EventStatus;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public record EventRequest(
        @NotNull @Positive Long locationId,
        @NotBlank @Size(min = 2) String eventName,
        @NotNull EventStatus eventStatus,
        @NotNull LocalDateTime eventStartDateTime,
        @NotNull LocalDateTime eventEndDateTime,
        @NotNull @PositiveOrZero Integer currentNumberOfPeople,
        @NotNull @Positive Integer capacity
) {
    public static EventRequest of(
            Long locationId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventRequest(
                locationId,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity
        );
    }

    public EventDto toDto() {
        return EventDto.of(
                null,
                null, //TODO: 여기를  반드시 적절하게 고쳐야 함
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                currentNumberOfPeople,
                capacity,
                null,
                null
        );
    }
}