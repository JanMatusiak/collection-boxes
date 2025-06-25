package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NonpositiveAmountException extends ResponseStatusException {
    public NonpositiveAmountException(){
        super(HttpStatus.BAD_REQUEST, "The value must be positive");
    }
}
