package com.example.covid.controller.api;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/locations")
@RestController
public class APIPlaceController {

    @GetMapping("")
    public List<String> getAllLocations() {
        return List.of("location1", "location2");
    }

    @PostMapping("")
    public Boolean createLocation() {
        return true;
    }

    @GetMapping("/{locationId}")
    public String getLocation(@PathVariable Integer locationId){
        return "location " + locationId;
    }

    @PutMapping("/{locationId}")
    public Boolean modifyLocation(@PathVariable Integer locationId) {
        return true;
    }

    @DeleteMapping("/{locationId}")
    public Boolean removeLocation(@PathVariable Integer locationId) {
        return true;
    }
}
