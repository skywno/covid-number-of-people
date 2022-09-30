package com.example.covid.controller;

import com.example.covid.constant.EventStatus;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Event;
import com.example.covid.dto.EventResponse;
import com.example.covid.dto.LocationDto;
import com.example.covid.service.EventService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/events")
@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ModelAndView events(@QuerydslPredicate(root= Event.class) Predicate predicate) {
        Map<String, Object> map = new HashMap<>();

        List<EventResponse> events = eventService.getEvents(predicate)
                .stream()
                .map(EventResponse::from)
                .toList();

        map.put("events", events);

        return new ModelAndView("event/index", map);
    }

    @GetMapping("/{eventId}")
    public ModelAndView eventDetail(@PathVariable Long eventId) {
        Map<String, Object> map = new HashMap<>();

        // TODO: 임시 데이터. 추후 삭제 예정
        map.put("event", EventResponse.of(
                eventId,
                LocationDto.of(
                        1L,
                        LocationType.SPORTS,
                        "배드민턴장",
                        "서울시 그리구 그래동",
                        "010-2222-3333",
                        33,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24
        ));

        return new ModelAndView("event/detail", map);
    }

}
