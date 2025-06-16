package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchEventException extends ResponseStatusException {
    public NoSuchEventException(String name){
        super(HttpStatus.NOT_FOUND, "There is no event named " + name);
    }
}
