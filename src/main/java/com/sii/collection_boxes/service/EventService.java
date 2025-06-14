package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.dto.FinancialReportDTO;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {


    EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public Long createEvent(CreateEventDTO dto){
        Event event = dto.toEvent();
        Event saved = eventRepository.save(event);
        return saved.getId();
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
