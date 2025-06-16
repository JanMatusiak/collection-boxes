package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NonpositiveAmountException extends ResponseStatusException {
    public NonpositiveAmountException(){
        super(HttpStatus.BAD_REQUEST, "The value must be positive");
    }
}
