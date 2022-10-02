package com.example.covid.repository;

import com.example.covid.domain.Location;
import com.example.covid.domain.QLocation;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface LocationRepository extends
        JpaRepository<Location, Long>,
        QuerydslPredicateExecutor<Location>,
        QuerydslBinderCustomizer<QLocation>
{
    @Override
    default void customize(QuerydslBindings bindings, QLocation root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.locationName, root.address,root.phoneNumber);
        bindings.bind(root.locationName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.address).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.phoneNumber).first(StringExpression::containsIgnoreCase);
    }

}