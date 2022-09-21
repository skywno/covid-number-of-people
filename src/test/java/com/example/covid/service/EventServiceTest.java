package com.example.covid.service;

import com.example.covid.constant.EventStatus;
import com.example.covid.dto.EventDTO;
import com.example.covid.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks EventService sut;

    @Mock EventRepository eventRepository;


    @DisplayName("검색 조건 없이 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_ThenReturnsEntireEventLists() {
        // Given
        given(eventRepository.findEvents(null, null, null, null, null))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", true),
                        createEventDTO(1L, "오후 운동", false)
                ));
        // When
        List<EventDTO> list = sut.getEvents(null, null, null, null, null);
        // Then
        assertThat(list).hasSize(2);
    }

    @DisplayName("검색 조건과 함께 이벤트를 검색하면, 검색 결과를 출력하여 보여준다.")
    @Test
    void givenSearchParameters_whenSearchingEvents_thenReturnsEventList() {
        // Given
        Long locationId = 1L;
        String eventName = "오전 운동";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDateTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDateTime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);

        given(eventRepository.findEvents(locationId, eventName, eventStatus, eventStartDateTime, eventEndDateTime))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", eventStatus, eventStartDateTime, eventEndDateTime)
                ));

        // When
        List<EventDTO> list = sut.getEvents(locationId, eventName, eventStatus, eventStartDateTime, eventEndDateTime);

        // Then
        assertThat(list)
                .hasSize(1)
                .allSatisfy(event -> {
                    assertThat(event)
                            .hasFieldOrPropertyWithValue("locationId", locationId)
                            .hasFieldOrPropertyWithValue("eventName", eventName)
                            .hasFieldOrPropertyWithValue("eventStatus", eventStatus);
                    assertThat(event.eventStartDateTime()).isAfterOrEqualTo(eventStartDateTime);
                    assertThat(event.eventEndDateTime()).isBeforeOrEqualTo(eventEndDateTime);
                });
        then(eventRepository).should(times(1)).findEvents(locationId, eventName, eventStatus, eventStartDateTime, eventEndDateTime);
    }

    @DisplayName("이벤트 ID가 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력한다.")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent() {
        // Given
        Long eventId = 1L;
        EventDTO eventDTO = createEventDTO(1L, "오전 운동", true);

        given(eventRepository.findEvent(eventId))
                .willReturn(Optional.of(eventDTO));
        // When
        Optional<EventDTO> result = sut.getEvent(eventId);
        // Then
        assertThat(result).hasValue(eventDTO);
        verify(eventRepository).findEvent(eventId);
    }

    @DisplayName("이벤트 ID가 존재하지 않는 이벤트를 조회하면, 빈 정보를 출력한다.")
    @Test
    void givenEventId_whenSearchingNonExistingEvent_thenReturnsEvent() {
        // Given
        Long eventId = 2L;
        given(eventRepository.findEvent(eventId))
                .willReturn(Optional.empty());
        // When
        Optional<EventDTO> result = sut.getEvent(eventId);
        // Then
        assertThat(result).isEmpty();
        verify(eventRepository).findEvent(eventId);
    }

    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다.")
    @Test
    void givenEventInfo_whenCreatingValidEvent_thenReturnsTrue() {
        // Given
        Long eventId = 1L;
        EventDTO eventDTO = createEventDTO(1L, "오전 운동", true);

        given(eventRepository.insertEvent(eventDTO))
                .willReturn(true);

        // When
        boolean result = sut.createEvent(eventDTO);

        // Then
        assertThat(result).isTrue();
        verify(eventRepository).insertEvent(eventDTO);
    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // Given
        given(eventRepository.insertEvent(null)).willReturn(false);

        // When
        boolean result = sut.createEvent(null);

        // Then
        assertThat(result).isFalse();
        verify(eventRepository).insertEvent(null);
    }

    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다.")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(eventId, dto)).willReturn(true);

        // When
        boolean result = sut.modifyEvent(eventId, dto);

        // Then
        assertThat(result).isTrue();
        verify(eventRepository).updateEvent(eventId, dto);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(null, dto)).willReturn(false);

        // When
        boolean result = sut.modifyEvent(null, dto);

        // Then
        assertThat(result).isFalse();
        verify(eventRepository).updateEvent(null, dto);
    }

    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long eventId = 1L;
        given(eventRepository.updateEvent(eventId, null)).willReturn(false);

        // When
        boolean result = sut.modifyEvent(eventId, null);

        // Then
        assertThat(result).isFalse();
        verify(eventRepository).updateEvent(eventId, null);
    }

    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다.")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        given(eventRepository.deleteEvent(eventId)).willReturn(true);

        // When
        boolean result = sut.removeEvent(eventId);

        // Then
        assertThat(result).isTrue();
        verify(eventRepository).deleteEvent(eventId);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // Given
        given(eventRepository.deleteEvent(null)).willReturn(false);

        // When
        boolean result = sut.removeEvent(null);

        // Then
        assertThat(result).isFalse();
        verify(eventRepository).deleteEvent(null);
    }

    private EventDTO createEventDTO(long locationId, String eventName, boolean isMorning) {
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEventDTO(
                locationId,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }

    private EventDTO createEventDTO(
            long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        return EventDTO.of(
                placeId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}