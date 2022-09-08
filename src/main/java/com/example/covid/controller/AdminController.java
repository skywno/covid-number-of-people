package com.example.covid.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/locations")
    public String adminPlaces() {
        return "admin/locations";
    }

    @GetMapping("/locations/{locationId}")
    public String adminPlaceDetail(@PathVariable Integer locationId) {
        return "admin/location-detail";
    }

    @GetMapping("/events")
    public String adminEvents() {
        return "admin/events";
    }

    @GetMapping("/events/{eventId}")
    public String adminEventDetail(@PathVariable Integer eventId) {
        return "admin/event-detail";
    }


}
