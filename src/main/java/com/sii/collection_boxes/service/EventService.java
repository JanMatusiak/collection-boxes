package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.events.CreateEventDTO;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.EventRepository;
import org.springframework.stereotype.Service;

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
}
