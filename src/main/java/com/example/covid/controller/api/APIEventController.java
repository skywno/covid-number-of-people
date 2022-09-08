package com.example.covid.controller.api;


import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events")
public class APIEventController {

    @GetMapping("")
    public List<String> getEvents() {
        return List.of("event1", "event2");
    }

    @PostMapping("")
    public Boolean createEvent() {
        return true;
    }

    @GetMapping("/{eventId}")
    public String getEvent(@PathVariable Integer eventId) {
        return "event " + eventId;
    }

    @PutMapping("/{eventId}")
    public Boolean modifyEvent(@PathVariable Integer eventId) {
        return true;
    }

    @DeleteMapping("{eventId}")
    public Boolean deleteEvent(@PathVariable Integer eventId){
        return true;
    }
}
