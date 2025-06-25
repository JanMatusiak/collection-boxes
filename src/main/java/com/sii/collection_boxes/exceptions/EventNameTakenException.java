package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventNameTakenException extends ResponseStatusException {
    public EventNameTakenException(String name){
        super(HttpStatus.BAD_REQUEST, "Name " + name + " is already taken");
    }
}
