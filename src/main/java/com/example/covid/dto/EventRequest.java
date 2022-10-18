package com.example.covid.dto;

import com.example.covid.constant.EventStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public record EventRequest(
        Long id,
        @NotBlank @Size(min = 2) String eventName,
        @NotNull EventStatus eventStatus,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDateTime,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDateTime,
        @NotNull @PositiveOrZero Integer currentNumberOfPeople,
        @NotNull @Positive Integer capacity
) {
    public static EventRequest of(
            Long id,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new EventRequest(
                id,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity
        );
    }

    public EventDto toDto(LocationDto locationDto) {
        return EventDto.of(
                this.id(),
                locationDto,
                this.eventName(),
                this.eventStatus(),
                this.eventStartDateTime(),
                this.eventEndDateTime(),
                this.currentNumberOfPeople(),
                this.capacity(),
                null,
                null
        );
    }
}