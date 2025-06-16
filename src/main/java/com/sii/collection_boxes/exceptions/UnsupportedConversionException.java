package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedConversionException extends ResponseStatusException {
    public UnsupportedConversionException(String pair) {
        super(HttpStatus.BAD_REQUEST, "Unsupported currency conversion: " + pair);
    }
}
