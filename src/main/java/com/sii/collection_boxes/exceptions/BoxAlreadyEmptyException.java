package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BoxAlreadyEmptyException extends ResponseStatusException {
    public BoxAlreadyEmptyException(Long id){
        super(HttpStatus.BAD_REQUEST, "Box " + id + " is already empty");
    }
}
