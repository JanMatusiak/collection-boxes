package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class BoxNotEmptyException extends ResponseStatusException {
    public BoxNotEmptyException(Long id){
        super(HttpStatus.BAD_REQUEST, "Box " + id + " is not empty");
    }
}
