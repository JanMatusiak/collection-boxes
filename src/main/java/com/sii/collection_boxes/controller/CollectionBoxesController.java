package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionBoxesController {
    CollectionBoxService collectionBoxService;

    public CollectionBoxesController(CollectionBoxService collectionBoxService){
        this.collectionBoxService = collectionBoxService;
    }

    @PostMapping("/registerBox")
    public ResponseEntity<String> registerBox(){
        Long id = collectionBoxService.registerBox();
        String msg = String.format("Box registered successfully with ID %d", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }
}
