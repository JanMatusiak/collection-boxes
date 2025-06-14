package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@Valid CreateEventDTO dto){
        Long id = eventService.createEvent(dto);
        String msg = String.format("Event %s created successfully with ID %d", dto.getName(), id);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }
}
