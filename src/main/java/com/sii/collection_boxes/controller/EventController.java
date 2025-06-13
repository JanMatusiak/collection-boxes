package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@RequestParam String name,
                                                      @RequestParam String currency){
        CreateEventDTO dto = new CreateEventDTO(name, currency);
        Event event = dto.toEvent();
        eventService.createEvent(event);
        String msg = String.format("Event created successfully with ID %d", event.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(msg);
    }
}
