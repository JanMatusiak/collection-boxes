package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedCurrencyException extends ResponseStatusException {
    public UnsupportedCurrencyException(String currency){
        super(HttpStatus.BAD_REQUEST, "Unsupported currency: " + currency);
    }
}
