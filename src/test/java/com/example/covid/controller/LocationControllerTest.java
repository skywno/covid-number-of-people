package com.example.covid.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    private MockMvc mvc;

    public LocationControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 장소 리스트 페이지")
    @Test
    void givenNothing_whenRequestingLocationsPage_thenReturnsLocationsPage() throws Exception {
        // Given

        // When && Then
        mvc.perform(get("/locations")
                        .contentType(MediaType.TEXT_HTML)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("location/index")   );

    }

    @DisplayName("[view][GET] 장소 세부 정보 페이지")
    @Test
    void givenNothing_whenRequestingLocationDetailPage_thenReturnsLocationDetailPage() throws Exception {
        /// Given
        Long locationId = 1L;
        // When
        mvc.perform(get("/locations/"+locationId))
                .andExpect(status().isOk())
                .andExpect(view().name("location/detail"));
        // Then
    }
}