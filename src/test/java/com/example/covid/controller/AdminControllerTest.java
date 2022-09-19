package com.example.covid.controller;

import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    private final MockMvc mvc;

    public AdminControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminPlacesPage_thenReturnsAdminAdminsPage() throws Exception {
        /// Given

        // When && Then
        mvc.perform(get("/admin/locations")
                .queryParam("locationType", LocationType.SPORTS.name())
                .queryParam("locationName", "랄라배드민턴장")
                .queryParam("address", "서울시 강남구 강남대로 1234"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/locations"));

    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰")
    @Test
    void givenPlaceId_whenRequestingAdminPlaceDetailPage_thenReturnsAdminPlaceDetailPage() throws Exception {
        // Given
        long placeId = 1L;

        // When & Then
        mvc.perform(get("/admin/locations/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/location-detail"));
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminEventsPage_thenReturnsAdminEventsPage() throws Exception {
        // Give

        // When && Then
        mvc.perform(get("/admin/events")
                .contentType(MediaType.TEXT_HTML)
                .queryParam("locationId", "1")
                .queryParam("locationName", "랄라배드민턴장")
                .queryParam("eventName", "오후 운동")
                .queryParam("eventStatus", EventStatus.OPENED.name())
                .queryParam("eventStartDateTime", LocalDateTime.now().minusDays(1).toString())
                .queryParam("eventEndDateTime", LocalDateTime.now().toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/events"));

    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰")
    @Test
    void givenEventId_whenRequestingAdminEventDetailPage_thenReturnsAdminEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;

        // When & Then
        mvc.perform(get("/admin/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/event-detail"));
    }
}