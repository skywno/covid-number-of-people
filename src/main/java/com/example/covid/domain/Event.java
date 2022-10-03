package com.example.covid.domain;

import com.example.covid.constant.EventStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "eventName"),
        @Index(columnList = "eventStartDateTime"),
        @Index(columnList = "eventEndDateTime"),
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
    private String eventName;

    @Setter
    @ManyToOne(optional = false)
    private Location location;

    @Setter
    @Column(nullable = false,
            columnDefinition = "varchar DEFAULT 'OPENED'")
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;


    @Setter
    @Column(nullable = false,
            columnDefinition = "datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventStartDateTime;

    @Setter
    @Column(nullable = false,
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

    public Event(String eventName, Location location, EventStatus status,
                 LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime,
                 Integer currentNumberOfPeople, Integer capacity) {
        this.eventName = eventName;
        this.location = location;
        this.eventStatus = status;
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
    }


    public static Event of(
            String eventName,
            Location location,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity
    ) {
        return new Event(
                eventName,
                location,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Event) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, eventStartDateTime, eventEndDateTime, createdAt, modifiedAt);
    }
}
