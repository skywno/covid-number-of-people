package com.example.covid.controller;

import com.example.covid.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void givenNothing_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        /// Given
        Long eventId = 1L;
        // When
        mvc.perform(get("/events/"+eventId))
                .andExpect(status().isOk())
                .andExpect(view().name("event/detail"));
        // Then
    }
}