package com.sii.collection_boxes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NonpositiveAmountException extends RuntimeException{
    public NonpositiveAmountException(){
        super("The value must be positive");
    }
}
