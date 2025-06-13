package com.sii.collection_boxes.service;

import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void createEvent(Event event){
        eventRepository.save(event);
    }
}
