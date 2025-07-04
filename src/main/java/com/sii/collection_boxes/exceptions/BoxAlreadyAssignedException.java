package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BoxAlreadyAssignedException extends ResponseStatusException {
    public BoxAlreadyAssignedException(Long id) {
        super(HttpStatus.BAD_REQUEST, "Box " + id + " is already assigned");
    }
}
