package com.example.covid.repository;

import com.example.covid.constant.EventStatus;
import com.example.covid.domain.Event;
import com.example.covid.domain.Location;
import com.example.covid.domain.QEvent;
import com.example.covid.dto.EventDto;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent>,
        EventRepositoryCustom
{
    Page<Event> findByLocation(Location location, Pageable pageable);
    @Override
    default void customize(QuerydslBindings bindings, QEvent root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.location.locationName, root.eventName, root.eventStatus,
                root.eventStartDateTime, root.eventEndDateTime);
        bindings.bind(root.eventName).first(StringExpression::contains);
        bindings.bind(root.location.locationName).as("locationName").first(StringExpression::containsIgnoreCase);
        bindings.bind(root.eventStartDateTime).first(ComparableExpression::goe);
        bindings.bind(root.eventEndDateTime).first(ComparableExpression::loe);
        bindings.bind(root.location.locationName).first(StringExpression::containsIgnoreCase);
    }

}
