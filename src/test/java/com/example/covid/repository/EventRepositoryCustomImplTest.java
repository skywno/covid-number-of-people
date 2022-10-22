package com.example.covid.repository;

import com.example.covid.constant.EventStatus;
import com.example.covid.dto.EventViewResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;


@Disabled("it fails when using environment variables, and I don't want to inject here directly. but this works fine")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {
    private final EventRepository sut;

    @Autowired
    public EventRepositoryTest(EventRepository eventRepository) {
        this.sut = eventRepository;
    }

    @DisplayName("이벤트 뷰 데이터를 검색 파라미터와 함께 조회하면, 조건에 맞는 데이터를 페이징 처리하여 리턴한다.")
    @Test
    void givenSearchParams_whenFindingEventViewPage_thenReturnsEventViewResponsePage() {
        // Given
        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                "레스토랑",
                "행사",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 01, 02, 0, 0),
                LocalDateTime.of(2021, 01, 02, 17, 0),
                PageRequest.of(0,5)

        );

        // Then
        assertThat(eventPage.getTotalPages()).isEqualTo(1);
        assertThat(eventPage.getNumberOfElements()).isEqualTo(2);
        assertThat(eventPage.getTotalElements()).isEqualTo(2);
        assertThat(eventPage.getContent().get(0))
                .hasFieldOrPropertyWithValue("locationName", "패캠 레스토랑")
                .hasFieldOrPropertyWithValue("eventName", "행사1")
                .hasFieldOrPropertyWithValue("eventStatus", EventStatus.OPENED)
                .hasFieldOrPropertyWithValue("eventStartDateTime", LocalDateTime.of(2021, 1, 2, 9, 0, 0))
                .hasFieldOrPropertyWithValue("eventEndDateTime", LocalDateTime.of(2021, 1, 2, 12, 0, 0));
    }

    @DisplayName("이벤트 뷰 데이터 검색어에 따른 조회 결과가 없으면, 빈 데이터를 페이징 정보와 함께 리턴한다.")
    @Test
    void givenWrongSearchParams_whenFindingEventViewPage_thenReturnsEmptyEventViewResponsePage() {
        // Given
        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                "없는 레스토랑",
                "없는 이벤트",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 01, 02, 0, 0),
                LocalDateTime.of(2021, 01, 02, 17, 0),
                PageRequest.of(0,5)
        );
        // Then
        assertThat(eventPage.isEmpty());
        assertThat(eventPage.getSize()).isEqualTo(5);
        assertThat(eventPage.getTotalElements()).isEqualTo(0);

        assertThat(eventPage).isEmpty();
        assertThat(eventPage).hasSize(0);
    }



    @DisplayName("이벤트 뷰 데이터를 검색 파라미터 없이 페이징 값만 주고 조회하면, 전체 데이터를 페이징 처리하여 리턴한다.")
    @Test
    void givenPagingInfoOnly_whenFindingEventViewPage_thenReturnsEventViewResponsePage() {
        // Given

        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 5)
        );

        // Then
        assertThat(eventPage).hasSize(5);
    }

    @DisplayName("이벤트 뷰 데이터를 페이징 정보 없이 조회하면, 에러를 리턴한다.")
    @Test
    void givenNothing_whenFindingEventViewPage_thenThrowsError() {
        // Given

        // When
        Throwable t = catchThrowable(() -> sut.findEventViewPageBySearchParams(
                null,
                null,
                null,
                null,
                null,
                null
        ));

        // Then
        assertThat(t).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}