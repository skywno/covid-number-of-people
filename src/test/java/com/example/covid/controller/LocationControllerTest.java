package com.example.covid.controller;

import com.example.covid.config.SecurityConfig;
import com.example.covid.dto.LocationDto;
import com.example.covid.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = LocationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes= SecurityConfig.class)
)
class LocationControllerTest {

    private MockMvc mvc;

    @MockBean
    private LocationService locationService;

    public LocationControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 장소 리스트 페이지")
    @Test
    void givenNothing_whenRequestingLocationsPage_thenReturnsLocationsPage() throws Exception {
        // Given

        given(locationService.getLocations(any())).willReturn(List.of());

        // When && Then
        mvc.perform(get("/locations")
                        .contentType(MediaType.TEXT_HTML)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("location/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("locations"));
        then(locationService).should().getLocations(any());

    }

    @DisplayName("[view][GET] 장소 세부 정보 페이지")
    @Test
    void givenNothing_whenRequestingLocationDetailPage_thenReturnsLocationDetailPage() throws Exception {
        /// Given
        Long locationId = 1L;
        given(locationService.getLocation(locationId))
                .willReturn(Optional.of(LocationDto.of(locationId,
                        null, null, null, null, null, null, null)));
        // When & Then
        mvc.perform(get("/locations/" + locationId))
                .andExpect(status().isOk())
                .andExpect(view().name("location/detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("location"));

        then(locationService).should().getLocation(locationId);
    }

    @DisplayName("[view][GET] 장소 세부 정보 페이지 - 데이터 없음")
    @Test
    void givenNonexistentPlaceId_whenRequestingPlaceDetailPage_thenReturnsErrorPage() throws Exception {
        // Given
        long locationId = 0L;
        given(locationService.getLocation(locationId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(get("/locations/" + locationId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(locationService).should().getLocation(locationId);
    }
}

