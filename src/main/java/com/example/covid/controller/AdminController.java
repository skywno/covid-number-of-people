package com.example.covid.controller;


import com.example.covid.constant.AdminOperationStatus;
import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Event;
import com.example.covid.domain.Location;
import com.example.covid.dto.*;
import com.example.covid.exception.GeneralException;
import com.example.covid.service.EventService;
import com.example.covid.service.LocationService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Validated
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final EventService eventService;
    private final LocationService locationService;

    @GetMapping("/locations")
    public ModelAndView adminLocations(
            @QuerydslPredicate(root = Location.class) Predicate predicate
    ) {

        List<LocationResponse> locations = locationService.getLocations(predicate)
                .stream()
                .map(LocationResponse::from)
                .toList();

        return new ModelAndView("admin/locations", Map.of(
                "locations", locations,
                "locationTypeOption", LocationType.values()
        ));
    }

    @GetMapping("/locations/{locationId}")
    public ModelAndView adminLocationDetail(@PathVariable Long locationId) {

        LocationResponse location = locationService.getLocation(locationId)
                .map(LocationResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));


        return new ModelAndView("admin/location-detail", Map.of(
                "adminOperationStatus", AdminOperationStatus.UPDATE,
                "location", location,
                "locationTypeOption", LocationType.values()
        ));
    }

    @GetMapping("/locations/new")
    public String newLocation(Model model) {
        model.addAttribute("adminOperationStatus", AdminOperationStatus.CREATE);
        model.addAttribute("locationTypeOption", LocationType.values());

        return "admin/location-detail";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/locations")
    public String upsertLocation(
            @Valid LocationRequest locationRequest,
            RedirectAttributes redirectAttributes
    ) {
        AdminOperationStatus status = locationRequest.id() != null ?
                AdminOperationStatus.UPDATE : AdminOperationStatus.CREATE;
        locationService.upsertLocation(locationRequest.toDto());

        redirectAttributes.addFlashAttribute("adminOperationStatus",
                status);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/locations");

        return "redirect:/admin/confirm";
    }

    @GetMapping("/locations/{locationId}/newEvent")
    public String newEvent(@PathVariable Long locationId, Model model) {
        EventResponse event = locationService.getLocation(locationId)
                .map(EventResponse::empty)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        model.addAttribute("adminOperationStatus", AdminOperationStatus.CREATE);
        model.addAttribute("eventStatusOption", EventStatus.values());
        model.addAttribute("event", event);

        return "admin/event-detail";
    }

    @PostMapping("/locations/{locationId}/events")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String upsertEvent(
            @Valid EventRequest eventRequest,
            @PathVariable Long locationId,
            RedirectAttributes redirectAttributes
    ) {
        AdminOperationStatus status = eventRequest.id() != null ?
                AdminOperationStatus.UPDATE : AdminOperationStatus.CREATE;
        eventService.upsertEvent(eventRequest.toDto(LocationDto.idOnly(locationId)));

        redirectAttributes.addFlashAttribute("adminOperationStatus",
                status);
        redirectAttributes.addFlashAttribute("redirectUrl",
                "/admin/events");

        return "redirect:/admin/confirm";
    }

    @GetMapping("/events")
    public ModelAndView adminEvents(
            @QuerydslPredicate(root = Event.class) Predicate predicate
    ) {

        List<EventResponse> responses = eventService.getEvents(predicate)
                .stream().map(EventResponse::from)
                .toList();

        return new ModelAndView("admin/events", Map.of(
                "events", responses,
                "eventStatusOption", EventStatus.values()
        ));
    }

    @GetMapping("/events/{eventId}")
    public ModelAndView adminEventDetail(@PathVariable Long eventId) {

        EventResponse eventResponse = eventService.getEvent(eventId)
                .map(EventResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        return new ModelAndView("admin/event-detail", Map.of(
                "adminOperationStatus", AdminOperationStatus.UPDATE,
                "event", eventResponse,
                "eventStatusOption", EventStatus.values()
        ));
    }

    @GetMapping("/confirm")
    public String confirm(Model model) {
        if (!model.containsAttribute("redirectUrl")) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        return "admin/confirm";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/events/{eventId}/delete")
    public String deleteEvent(
            @PathVariable Long eventId,
            RedirectAttributes redirectAttributes
    ) {
        eventService.removeEvent(eventId);

        redirectAttributes.addFlashAttribute("adminOperationStatus",
                AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/events");

        return "redirect:/admin/confirm";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/locations/{locationId}/delete")
    public String deleteLocation(
            @PathVariable Long locationId,
            RedirectAttributes redirectAttributes
    ) {
        locationService.removeLocation(locationId);

        redirectAttributes.addFlashAttribute("adminOperationStatus",
                AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/locations");

        return "redirect:/admin/confirm";
    }
}
