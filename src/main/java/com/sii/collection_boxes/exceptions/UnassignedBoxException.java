package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnassignedBoxException extends RuntimeException {
    public UnassignedBoxException(Long id) {
        super("Box " + id + " is not assigned");
    }
}
