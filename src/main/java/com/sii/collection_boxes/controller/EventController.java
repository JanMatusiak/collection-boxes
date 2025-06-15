package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class EventController {

    private final EventService eventService;
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@Valid CreateEventDTO dto){
        eventService.createEvent(dto);
        log.info("Event {} created successfully", dto.name());
        String msg = String.format("Event %s created successfully", dto.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }
}
