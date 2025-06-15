package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSuchEventException extends RuntimeException{
    public NoSuchEventException(String name){
        super("There is no event named " + name);
    }
}
