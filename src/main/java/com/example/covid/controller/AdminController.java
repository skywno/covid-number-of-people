package com.example.covid.controller;


import com.example.covid.constant.LocationType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/locations")
    public ModelAndView adminPlaces(
            LocationType locationType, String locationName, String address
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put("locationType", locationType);
        model.put("locationName", locationName);
        model.put("address", address);

        return new ModelAndView("admin/locations", model);
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
