package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.boxes.CollectionBoxesStateDTO;
import com.sii.collection_boxes.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/listBoxes")
    public ResponseEntity<List<CollectionBoxesStateDTO>> listBoxes(){
        List<CollectionBoxesStateDTO> dtoList = collectionBoxService.listBoxes();
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/unregisterBox/{id}")
    public ResponseEntity<String> unregisterBox(@PathVariable Long id){
        collectionBoxService.unregisterBox(id);
        String msg = String.format("Box with ID %d unregistered successfully", id);
        return ResponseEntity.ok(msg);
    }
}
