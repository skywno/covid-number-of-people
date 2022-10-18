package com.example.covid.service;


import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.EventStatus;
import com.example.covid.domain.Location;
import com.example.covid.dto.EventDto;
import com.example.covid.dto.EventViewResponse;
import com.example.covid.dto.LocationDto;
import com.example.covid.dto.LocationResponse;
import com.example.covid.exception.GeneralException;
import com.example.covid.repository.EventRepository;
import com.example.covid.repository.LocationRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Transactional
@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;


    @Transactional(readOnly = true)
    public List<EventDto> getEvents(Predicate predicate) {
        try {
            return StreamSupport.stream(eventRepository.findAll(predicate).spliterator(), false)
                    .map(EventDto::of)
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    @Transactional(readOnly = true)
    public Page<EventViewResponse> getEventViewResponse(
            String locationName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Pageable pageable
    ) {
        try {
            return eventRepository.findEventViewPageBySearchParams(
                    locationName,
                    eventName,
                    eventStatus,
                    eventStartDateTime,
                    eventEndDateTime,
                    pageable
            );
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Optional<EventDto> getEvent(Long eventId) {
        try {
            return eventRepository.findById(eventId).map(EventDto::of);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    @Transactional
    public boolean createEvent(EventDto eventDto) {
        try {
            if (eventDto == null) {
                return false;
            }

            Location location = locationRepository.findById(eventDto.locationDto().id())
                    .orElseThrow(() -> new GeneralException(ErrorCode.DATA_ACCESS_ERROR));

            eventRepository.save(eventDto.toEntity(location));
            return true;

        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    @Transactional
    public boolean modifyEvent(Long eventId, EventDto dto) {
        try {
            if (eventId == null || dto == null) {
                return false;
            }

            eventRepository.findById(eventId).ifPresent(event -> eventRepository.save(dto.updateEntity(event)));
            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    @Transactional
    public boolean removeEvent(Long eventId) {
        try {
            if (eventId == null) {
                return false;
            }

            eventRepository.deleteById(eventId);
            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean upsertEvent(EventDto eventDto){
        try{
            if (eventDto.id() != null){
                return modifyEvent(eventDto.id(), eventDto);
            } else {
                return createEvent(eventDto);
            }
        } catch (Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }
}
