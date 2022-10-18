package com.example.covid.controller;

import com.example.covid.constant.AdminOperationStatus;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.dto.*;
import com.example.covid.service.EventService;
import com.example.covid.service.LocationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    private EventService eventService;
    @MockBean
    private LocationService locationService;

    public AdminControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminPlacesPage_thenReturnsAdminAdminsPage() throws Exception {
        /// Given
        given(locationService.getLocations(any()))
                .willReturn(List.of());
        // When && Then
        mvc.perform(get("/admin/locations")
                        .queryParam("locationType", LocationType.SPORTS.name())
                        .queryParam("locationName", "랄라배드민턴장")
                        .queryParam("address", "서울시 강남구 강남대로 1234")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/locations"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("locations"))
                .andExpect(model().attributeExists("locationTypeOption"))
                .andExpect(model().attribute("locationTypeOption",
                        LocationType.values()));

        then(locationService).should().getLocations(any());

    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰")
    @Test
    void givenPlaceId_whenRequestingAdminPlaceDetailPage_thenReturnsAdminPlaceDetailPage() throws Exception {
        // Given
        long placeId = 1L;
        given(locationService.getLocation(placeId))
                .willReturn(Optional.of(
                        LocationDto.of(placeId, null,
                                null, null,
                                null, null,
                                null, null)
                ));
        // When & Then
        mvc.perform(get("/admin/locations/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/location-detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("location"))
                .andExpect(model().attribute("adminOperationStatus",
                        AdminOperationStatus.UPDATE))
                .andExpect(model().attribute("locationTypeOption",
                        LocationType.values()));

        then(locationService).should().getLocation(placeId);

    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰, 데이터 없음")
    @Test
    void givenNonexistentPlaceId_whenRequestingAdminPlaceDetailPage_thenReturnsErrorPage() throws Exception {
        // Given
        long placeId = 1L;
        given(locationService.getLocation(placeId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(get("/admin/locations/" + placeId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));

        then(locationService).should().getLocation(placeId);

    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 새로 만들기 뷰")
    @Test
    void givenNothing_whenRequestingNewLocationPage_thenReturnsNewLocationPage() throws Exception {
        // Given
        // When & Then
        mvc.perform(get("/admin/locations/new"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("adminOperationStatus",
                        AdminOperationStatus.CREATE))
                .andExpect(model().attribute("locationTypeOption",
                        LocationType.values()));
    }


    @DisplayName("[view][POST] 어드민 페이지 - 장소 세뷰 정보 뷰, 장소 저장")
    @Test
    void givenNewLocation_whenSavingLocation_thenSavesLocationAndReturnsToListPage() throws Exception {
        // Given
        LocationRequest lr = LocationRequest.of(LocationType.SPORTS, "강남 배드민턴장", "서울시" +
                " 강남구 강남동", "010-1231-2312", 10);

        given(locationService.createLocation(lr.toDto()))
                .willReturn(true);
        // When
        mvc.perform(post("/admin/locations")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectToFormData(lr)))
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(flash().attribute("adminOperationStatus",
                        AdminOperationStatus.CREATE))
                .andExpect(flash().attribute("redirectUrl", "/admin/locations"));

        // Then
        then(locationService).should().createLocation(lr.toDto());
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminEventsPage_thenReturnsAdminEventsPage() throws Exception {
        // Given
        given(eventService.getEvents(any())).willReturn(List.of());
        // When && Then
        mvc.perform(get("/admin/events")
                        .contentType(MediaType.TEXT_HTML)
                        .queryParam("locationId", "1")
                        .queryParam("locationName", "랄라배드민턴장")
                        .queryParam("eventName", "오후 운동")
                        .queryParam("eventStatus", EventStatus.OPENED.name())
                        .queryParam("eventStartDateTime",
                                LocalDateTime.now().minusDays(1).toString())
                        .queryParam("eventEndDateTime", LocalDateTime.now().toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/events"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("eventStatusOption"));

        then(eventService).should().getEvents(any());

    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰")
    @Test
    void givenEventId_whenRequestingAdminEventDetailPage_thenReturnsAdminEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.of(
                EventDto.of(eventId, null, null, null, null, null, null, null, null,
                        null)
        ));

        // When & Then
        mvc.perform(get("/admin/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/event-detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("eventStatusOption"));

        then(eventService).should().getEvent(eventId);
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰, 데이터 없음")
    @Test
    void givenNonexistentEventId_whenRequestingAdminEventDetailPage_thenReturnsErrorPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(get("/admin/events/" + eventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(eventService).should().getEvent(eventId);
    }

    @DisplayName("[view][get] 어드민 페이지 - 새로운 이벤트 뷰")
    @Test
    void givenNothing_whenRequestingNewEventPage_thenReturnsNewEventPage() throws Exception {
        // Given
        long locationId = 1L;
        LocationDto locationDto = LocationDto.of(null, null,
                "test name", null, null,
                null, null, null);
        EventResponse expectedEventResponse = EventResponse.empty(locationDto);

        given(locationService.getLocation(locationId))
                .willReturn(Optional.of(locationDto));
        // When & Then
        mvc.perform(get("/admin/locations/" + locationId + "/newEvent"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/event-detail"))
                .andExpect(model().attribute("adminOperationStatus",
                        AdminOperationStatus.CREATE))
                .andExpect(model().attribute("eventStatusOption", EventStatus.values()))
                .andExpect(model().attribute("event", expectedEventResponse));

        then(locationService).should().getLocation(locationId);
    }

    @DisplayName("[view][POST] 어드민 페이지 - 이벤트 세부 정보 뷰, 이벤트 저장")
    @Test
    void givenEventRequest_whenCreatingNewEvent_thenSavesEventAndReturnsToListPage() throws Exception {
        long locationId = 1L;
        EventRequest eventRequest = EventRequest.of("test Event", EventStatus.OPENED,
                LocalDateTime.now(), LocalDateTime.now(), 10, 10);

        given(eventService.createEvent(eventRequest.toDto(LocationDto.idOnly(locationId))))
                .willReturn(true);

        // When & Then
        mvc.perform(post("/admin/locations/" + locationId + "/events")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectToFormData(eventRequest)))
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(flash().attribute("redirectUrl",
                        "/admin/locations/" + locationId))
                .andExpect(flash().attribute("adminOperationStatus",
                        AdminOperationStatus.CREATE))
                .andDo(print());

        then(eventService).should().createEvent(eventRequest.toDto(LocationDto.idOnly(locationId)));

    }

    @DisplayName("[view][GET] 어드민 페이지 - 기능 확인 페이지")
    @Test
    void given_whenAfterOperation_thenRedirectsToPage() throws Exception {
        // Given
        // When & Then
        mvc.perform(get("/admin/confirm")
                        .flashAttr("adminOperationStatus", AdminOperationStatus.CREATE)
                        .flashAttr("redirectUrl", "/admin/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/confirm"))
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(model().attribute("redirectUrl", "/admin/locations"))
                .andDo(print());
    }

    private String objectToFormData(Object obj) {
        Map<String, String> map = mapper.convertValue(obj, new TypeReference<>() {
        });

        return map.entrySet().stream()
                .map(entry -> entry.getValue() == null ? "" :
                        entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),
                                StandardCharsets.UTF_8))
                .filter(str -> !str.isBlank())
                .collect(Collectors.joining("&"));
    }
}