package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchBoxException extends ResponseStatusException {
    public NoSuchBoxException(Long id){
        super(HttpStatus.NOT_FOUND, "There is no box with ID " + id);
    }
}
