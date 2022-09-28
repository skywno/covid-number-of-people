package com.example.covid.repository;

import com.example.covid.domain.Event;
import com.example.covid.domain.QEvent;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent>
{

    @Override
    default void customize(QuerydslBindings bindings, QEvent root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.locationId, root.name, root.status,
                root.eventStartDateTime, root.eventEndDateTime);
        bindings.bind(root.name).first(StringExpression::likeIgnoreCase);
        bindings.bind(root.eventStartDateTime).first(ComparableExpression::goe);
        bindings.bind(root.eventEndDateTime).first(ComparableExpression::loe);
    }


}
