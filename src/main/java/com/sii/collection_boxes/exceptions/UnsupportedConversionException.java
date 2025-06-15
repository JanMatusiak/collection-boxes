package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedConversionException extends RuntimeException {
    public UnsupportedConversionException(String pair) {
        super("Unsupported currency conversion: " + pair);
    }
}
