package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.dto.FinancialReportDTO;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.entity.SupportedCurrencies;
import com.sii.collection_boxes.repository.EventRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {


    EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public void createEvent(CreateEventDTO dto){
        if(eventRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Event name already in use: " + dto.getName());
        }
        if (!EnumUtils.isValidEnum(SupportedCurrencies.class, dto.getCurrency())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Unsupported currency: " + dto.getCurrency());
        }
        Event event = dto.toEvent();
        eventRepository.save(event);
    }

    public List<FinancialReportDTO> displayReport(){
        return eventRepository.findAll().stream()
                .map(e -> new FinancialReportDTO(
                        e.getName(),
                        e.getBalance(),
                        e.getCurrency()))
                .collect(Collectors.toList());
    }
}
