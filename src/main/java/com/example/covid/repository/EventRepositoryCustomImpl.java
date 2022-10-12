package com.example.covid.repository;

import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.domain.Event;
import com.example.covid.domain.QEvent;
import com.example.covid.dto.EventViewResponse;
import com.example.covid.exception.GeneralException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventRepositoryCustomImpl extends QuerydslRepositorySupport implements EventRepositoryCustom {

    public EventRepositoryCustomImpl() {
        super(Event.class);
    }

    @Override
    public Page<EventViewResponse> findEventViewPageBySearchParams(
            String locationName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Pageable pageable
    ) {
        QEvent event = QEvent.event;

        JPQLQuery<EventViewResponse> query = from(event).select(Projections.constructor(
                EventViewResponse.class,
                event.id,
                event.location.locationName,
                event.eventName,
                event.eventStatus,
                event.eventStartDateTime,
                event.eventEndDateTime,
                event.currentNumberOfPeople,
                event.capacity
        ));

        if (locationName != null && !locationName.isBlank())
            query.where(event.location.locationName.containsIgnoreCase(locationName));

        if (eventName != null && !eventName.isBlank())
            query.where(event.eventName.containsIgnoreCase(eventName));

        if (eventStatus != null)
            query.where(event.eventStatus.eq(eventStatus));

        if (eventStartDateTime != null)
            query.where(event.eventStartDateTime.goe(eventStartDateTime));

        if (eventEndDateTime != null)
            query.where(event.eventEndDateTime.loe(eventEndDateTime));

        List<EventViewResponse> eventViewResponses =
                Optional.ofNullable(getQuerydsl()).orElseThrow(
                                () -> new GeneralException(ErrorCode.DATA_ACCESS_ERROR, "Spring data JPA로부터 " + "QueryDsl 인스턴스를 가져올 수 없음.")
                        ).applyPagination(pageable, query)
                        .fetch();

        return new PageImpl<>(eventViewResponses, pageable, query.fetchCount());
    }
}
