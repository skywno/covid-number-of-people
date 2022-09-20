package com.example.covid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/locations")
@Controller
public class LocationController {

    @GetMapping
    public String places() {
        return "location/index";
    }

    @GetMapping("/{locationId}")
    public String placeDetail(@PathVariable Long locationId) {
        return "location/detail";
    }
}
