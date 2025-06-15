package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchBoxException extends RuntimeException {
    public NoSuchBoxException(Long id){
        super("There is no box with ID " + id);
    }
}
