package com.example.covid.domain;

import com.example.covid.constant.EventStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "locationId"),
        @Index(columnList = "eventName"),
        @Index(columnList = "eventStartDatetime"),
        @Index(columnList = "eventEndDatetime"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Event {

    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private Long locationId;

    @Setter
    @Column(insertable = false, nullable = false,
            columnDefinition = "varchar DEFAULT 'OPENED'")
    private EventStatus status;


    @Setter
    @Column(insertable = false, updatable = false, nullable = false,
            columnDefinition = "datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventStartDateTime;

    @Setter
    @Column(insertable = false, updatable = false, nullable = false,
            columnDefinition = "datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventEndDateTime;

    @Setter
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer currentNumberOfPeople;
    @Setter
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP on update " +
                    "CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    protected Event() {
    }

    public Event(String name, Long locationId, EventStatus status,
                 LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime,
                 Integer currentNumberOfPeople, Integer capacity) {
        this.name = name;
        this.locationId = locationId;
        this.status = status;
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
    }


    public static Event of(
            String eventName,
            Long locationId,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new Event(
                eventName,
                locationId,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity
        );
    }
}
