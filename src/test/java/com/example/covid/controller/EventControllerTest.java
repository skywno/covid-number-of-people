package com.example.covid.controller;

import com.example.covid.constant.EventStatus;
import com.example.covid.dto.EventDto;
import com.example.covid.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EventController.class)
class EventControllerTest {

    @MockBean private EventService eventService;
    private final MockMvc mvc;

    public EventControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지")
    @Test
    void givenNothing_whenRequestingEventsPage_thenReturnsEventsPage() throws Exception {
        // Given

        // When && Then
        mvc.perform(get("/events")
                .contentType(MediaType.TEXT_HTML)
            )
                .andExpect(status().isOk())
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));


    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터")
    @Test
    void givenNothing_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        // Given
        given(eventService.getEventViewResponse(any(), any(), any(), any(), any(), any()))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/events/custom"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEventViewResponse(any(), any(), any(), any(), any(), any());
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터 + 검색 파라미터")
    @Test
    void givenParams_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        // Given
        String locationName = "배드민턴";
        String eventName = "오후";

        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);
        given(eventService.getEventViewResponse(
                locationName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                PageRequest.of(1, 3)
        )).willReturn(Page.empty());

        // When & Then
        mvc.perform(
                        get("/events/custom")
                                .queryParam("locationName", locationName)
                                .queryParam("eventName", eventName)
                                .queryParam("eventStatus", eventStatus.name())
                                .queryParam("eventStartDatetime", eventStartDatetime.toString())
                                .queryParam("eventEndDatetime", eventEndDatetime.toString())
                                .queryParam("page", "1")
                                .queryParam("size", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEventViewResponse(
                locationName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                PageRequest.of(1, 3)
        );
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터 + 검색 파라미터 (장소명, 이벤트명 잘못된 입력)")
    @Test
    void givenWrongParams_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        // Given
        String placeName = "d";
        String eventName = "오";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);
        given(eventService.getEventViewResponse(
                placeName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                PageRequest.of(1, 3)
        )).willReturn(Page.empty());

        // When & Then
        mvc.perform(
                        get("/events/custom")
                                .queryParam("locationName", placeName)
                                .queryParam("eventName", eventName)
                                .queryParam("eventStatus", eventStatus.name())
                                .queryParam("eventStartDatetime", eventStartDatetime.toString())
                                .queryParam("eventEndDatetime", eventEndDatetime.toString())
                                .queryParam("page", "1")
                                .queryParam("size", "3")
                )
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"))
                .andExpect(model().attributeDoesNotExist("events"));
        then(eventService).shouldHaveNoInteractions();
    }

    @Test
    void givenNothing_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        /// Given
        Long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.of(EventDto.of(
                1L, null, null, null, null, null, null, null, null, null)
        ));
        // When
        mvc.perform(get("/events/"+eventId))
                .andExpect(status().isOk())
                .andExpect(view().name("event/detail"));
        // Then
    }
}