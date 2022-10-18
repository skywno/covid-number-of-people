package com.example.covid.controller.api;

import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventRequest;
import com.example.covid.dto.LocationDto;
import com.example.covid.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("API 컨트롤러가 필요없는 상황이어서 비활성화")
@WebMvcTest(APIEventController.class)
class APIEventControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    EventService eventService;

    public APIEventControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API][GET] 이벤트 리스트 조회 + 올바른 검색 파라미터")
    @Test
    void givenValidParams_whenRequestingEvents_thenReturnsListOfEventsInStandardResponse() throws Exception {
        /// Given

        // When & Then
        mvc.perform(get("/api/events")
                        .queryParam("locationId", "1")
                        .queryParam("eventName", "오후 운동")
                        .queryParam("eventStatus", EventStatus.OPENED.name())
                        .queryParam("eventStartDateTime", "2021-01-01T00:00:00")
                        .queryParam("eventEndDateTime", "2021-01-02T00:00:00")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].locationId")
                        .value(1L))
                .andExpect(jsonPath("$.data[0].eventName")
                        .value("오후 운동"))
                .andExpect(jsonPath("$.data[0].eventStatus")
                        .value(EventStatus.OPENED.name()))
                .andExpect(jsonPath("$.data[0].eventStartDateTime")
                        .value(LocalDateTime
                                .of(2021, 1, 1, 13, 0, 0)
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data[0].eventEndDateTime")
                        .value(LocalDateTime
                                .of(2021, 1, 1, 16, 0, 0)
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data[0].currentNumberOfPeople")
                        .value(0))
                .andExpect(jsonPath("$.data[0].capacity")
                        .value(24))
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.errorCode")
                        .value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][GET] 이벤트 리스트 조회 + 잘못된 검색 파라미터")
    @Test
    void givenInvalidParams_whenRequestingEvents_thenReturnsListOfFailedStandardResponse() throws Exception {
        /// Given
        // When & Then
        mvc.perform(get("/api/events")
                        .queryParam("locationId", "-1")
                        .queryParam("eventName", "오후 운동")
                        .queryParam("eventStatus", EventStatus.OPENED.name())
                        .queryParam("eventStartDateTime", "2021-01-01T00:00:00")
                        .queryParam("eventEndDateTime", "2021-01-02T00:00:00")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success")
                        .value(false))
                .andExpect(jsonPath("$.errorCode")
                        .value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message")
                        .value(containsString(ErrorCode.VALIDATION_ERROR.getMessage())));
        then(eventService).shouldHaveNoInteractions();
    }

    @DisplayName("[API][POST] 이벤트 생성")
    @Test
    void givenEvent_whenCreatingAnEvent_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        given(eventService.createEvent(any())).willReturn(true);
        EventRequest eventRequest = EventRequest.of(
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        );

        // When & Then
        mvc.perform(
                        post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(eventRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(Boolean.toString(true)))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        then(eventService).should().createEvent(any());
    }

    @DisplayName("[API][POST] 이벤트 생성 + 잘못된 정보 입력")
    @Test
    void givenInvalidEvent_whenCreatingAnEvent_thenReturnsFailedStandardResponse() throws Exception {
        // Given
        EventRequest eventRequest = EventRequest.of(
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        );

        // When & Then
        mvc.perform(
                        post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(eventRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(Boolean.toString(false)))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.SPRING_BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(containsString("Field error in object 'eventRequest'")));
        then(eventService).shouldHaveNoInteractions();
    }


    @DisplayName("[API][GET] 단일 이벤트 조회 - 이벤트 있는 경우, 이벤트 데이터를 담은 표준 API 출력")
    @Test
    void givenEventId_whenRequestingExistentEvent_thenReturnsEventInStandardResponse()
            throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.of(createEventDto()));

        // When & Then
        mvc.perform(get("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.locationId").value(1L))
                .andExpect(jsonPath("$.data.eventName").value("오후 운동"))
                .andExpect(jsonPath("$.data.eventStatus").value(EventStatus.OPENED.name()))
                .andExpect(jsonPath("$.data.eventStartDateTime").value(LocalDateTime
                        .of(2021, 1, 1, 13, 0, 0)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.eventEndDateTime").value(LocalDateTime
                        .of(2021, 1, 1, 16, 0, 0)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.data.currentNumberOfPeople").value(0))
                .andExpect(jsonPath("$.data.capacity").value(24))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));

        then(eventService).should().getEvent(any());
    }

    @DisplayName("[API][GET] 단일 이벤트 조회 - 이벤트 없는 경우, 빈 표준 API 출력")
    @Test
    void givenEventId_whenRequestingNonexistentEvent_thenReturnsEmptyStandardResponse() throws Exception {
        // Given
        long eventId = 2L;
        given(eventService.getEvent(eventId)).willReturn(Optional.empty());
        // When & Then
        mvc.perform(get("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        then(eventService).should().getEvent(any());
    }

    @DisplayName("[API][PUT] 이벤트 변경")
    @Test
    void givenEvent_whenModifyingAnEvent_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        long eventId = 1L;
        EventDto dto = createEventDto();
        EventRequest eventRequest = EventRequest.of(
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        );

        given(eventService.modifyEvent(eventId, dto)).willReturn(true);

        // When & Then
        mvc.perform(
                        put("/api/events/" + eventId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(eventRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        then(eventService).should().modifyEvent(any(), any());

    }

    @DisplayName("[API][PUT] 이벤트 변경 + 잘못된 정보 기입")
    @Test
    void givenWrongEvent_whenModifyingAnEvent_thenReturnsFailedStandardResponse() throws Exception {
        // Given
        long eventId = 1L;
        EventDto dto = createEventDto();
        EventRequest eventRequest = EventRequest.of(
                null,
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        );

        // When & Then
        mvc.perform(
                        put("/api/events/" + eventId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(eventRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.SPRING_BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(containsString("null")));
        then(eventService).shouldHaveNoInteractions();

    }
    @DisplayName("[API][DELETE] 이벤트 삭제")
    @Test
    void givenEvent_whenDeletingAnEvent_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.removeEvent(eventId))
                .willReturn(true);

        // When & Then
        mvc.perform(delete("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        then(eventService).should().removeEvent(any());

    }

    private EventDto createEventDto() {
        return EventDto.of(
                1L,
                createLocationDto(0L),
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private LocationDto createLocationDto(long locationId) {
        return LocationDto.of(locationId,
                LocationType.COMMON,
                "배드민턴장",
                "서울시 가나구 다라동",
                "010-1111-2222",
                10,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

    }

}