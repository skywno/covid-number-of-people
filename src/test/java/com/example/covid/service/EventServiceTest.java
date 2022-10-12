package com.example.covid.service;

import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Event;
import com.example.covid.domain.Location;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventViewResponse;
import com.example.covid.exception.GeneralException;
import com.example.covid.repository.EventRepository;
import com.example.covid.repository.LocationRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    EventService sut;

    @Mock
    EventRepository eventRepository;
    @Mock
    LocationRepository locationRepository;


    @DisplayName("이벤트를 검색하면, 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_ThenReturnsEntireEventLists() {
        // Given
        given(eventRepository.findAll(any(Predicate.class)))
                .willReturn(List.of(
                        createEvent("오전 운동", true),
                        createEvent("오후 운동", false)
                ));
        // When
        List<EventDto> list = sut.getEvents(new BooleanBuilder());
        // Then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("이벤트 뷰 페이지를 검색하면, 페이징된 결과를 출력하여 보여준다")
    @Test
    void givenNothing_whenSearchingEventViewPage_returnsEventViewPage() {
        // Given
        given(eventRepository.findEventViewPageBySearchParams(
                null,
                null,
                null,
                null
                , null,
                PageRequest.ofSize(10)))
                .willReturn(new PageImpl<>(List.of(
                        EventViewResponse.from(EventDto.of(createEvent("오전 운동", true))),
                        EventViewResponse.from(EventDto.of(createEvent("오후 운동",
                                false))))
                ));
        // When
        Page<EventViewResponse> list = sut.getEventViewResponse(null,
                null,
                null,
                null,
                null,
                PageRequest.ofSize(10));
        // Then

        assertThat(list).hasSize(2);
        then(eventRepository).should().findEventViewPageBySearchParams(
                null,
                null,
                null,
                null,
                null,
                PageRequest.ofSize(10));
    }

    @DisplayName("이벤트 ID가 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력한다.")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent() {
        // Given
        Long eventId = 1L;
        Event event = createEvent("오전 운동", true);

        given(eventRepository.findById(eventId))
                .willReturn(Optional.of(event));
        // When
        Optional<EventDto> result = sut.getEvent(eventId);
        // Then
        assertThat(result).hasValue(EventDto.of(event));
        verify(eventRepository).findById(eventId);
    }

    @DisplayName("이벤트 ID가 존재하지 않는 이벤트를 조회하면, 빈 정보를 출력한다.")
    @Test
    void givenEventId_whenSearchingNonExistingEvent_thenReturnsEvent() {
        // Given
        Long eventId = 2L;
        given(eventRepository.findById(eventId))
                .willReturn(Optional.empty());
        // When
        Optional<EventDto> result = sut.getEvent(eventId);
        // Then
        assertThat(result).isEmpty();
        verify(eventRepository).findById(eventId);
    }

    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다.")
    @Test
    void givenEventInfo_whenCreatingValidEvent_thenReturnsTrue() {
        // Given
        Long eventId = 1L;
        Event event = createEvent("오전 운동", true);
        EventDto eventDto = EventDto.of(event);

        given(locationRepository.findById(eventDto.locationDto().id())).willReturn(Optional.of(createLocation()));
        given(eventRepository.save(any(Event.class)))
                .willReturn(event);

        // When
        boolean result = sut.createEvent(EventDto.of(event));

        // Then
        assertThat(result).isTrue();
        then(locationRepository).should().findById(eventDto.locationDto().id());
        then(eventRepository).should().save(any(Event.class));
    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // Given

        // When
        boolean result = sut.createEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 생성 중 장소 정보가 틀리거나 없으면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다")
    @Test
    void givenWrongPlaceId_whenCreating_thenThrowsGeneralException() {
        // Given
        Event event = createEvent(null, false);
        given(locationRepository.findById(event.getLocation().getId())).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> sut.createEvent(EventDto.of(event)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().findById(event.getLocation().getId());
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 생성 중 데이터 예외가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다")
    @Test
    void givenDataRelatedException_whenCreating_thenThrowsGeneralException() {
        // Given
        Event event = createEvent(null, false);
        RuntimeException e = new RuntimeException("This is test.");
        given(locationRepository.findById(any())).willThrow(e);

        // When
        ProcessHandle EventDTO;
        Throwable thrown = catchThrowable(() -> sut.createEvent(EventDto.of(event)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().findById(any());
    }

    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다.")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        Event originalEvent = createEvent("오후 운동", false);
        Event changedEvent = createEvent("오전 운동", true);
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));

        // When
        boolean result = sut.modifyEvent(eventId, EventDto.of(changedEvent));

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(any(Event.class));
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        Event originalEvent = createEvent("오후 운동", false);
        Event changedEvent = createEvent("오전 운동", true);

        // When
        boolean result = sut.modifyEvent(null, EventDto.of(changedEvent));

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long eventId = 1L;

        // When
        boolean result = sut.modifyEvent(eventId, null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 변경 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenModifying_thenThrowsGeneralException() {
        // Given
        long eventId = 1L;
        Event originalEvent = createEvent("오후 운동", false);
        Event wrongEvent = createEvent(null, false);
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));
        given(eventRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.modifyEvent(eventId,
                EventDto.of(wrongEvent)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(any());
    }

    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다.")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        willDoNothing().given(eventRepository).deleteById(eventId);
        // When

        boolean result = sut.removeEvent(eventId);

        // Then
        assertThat(result).isTrue();
        verify(eventRepository).deleteById(eventId);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // Given

        // When
        boolean result = sut.removeEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 삭제 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenDeleting_thenThrowsGeneralException() {
        // Given
        long eventId = 0L;
        RuntimeException e = new RuntimeException("This is test.");
        willThrow(e).given(eventRepository).deleteById(eventId);

        // When
        Throwable thrown = catchThrowable(() -> sut.removeEvent(eventId));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().deleteById(eventId);
    }

    private Event createEvent(String eventName, boolean isMorning) {
        Location location = Location.of(LocationType.COMMON, "테스트 이름", "테스트 주소", "테스트" +
                " 폰넘버", 30);
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEvent(
                location,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }

    private Event createEvent(
            Location location,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        return Event.of(
                eventName,
                location,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24
        );
    }

    private Location createLocation() {
        return createLocation(1L);
    }

    private Location createLocation(long id) {
        Location location = Location.of(LocationType.COMMON, "test place", "test " +
                "address", "010-1234-1234", 10);
        ReflectionTestUtils.setField(location, "id", id);

        return location;
    }
}