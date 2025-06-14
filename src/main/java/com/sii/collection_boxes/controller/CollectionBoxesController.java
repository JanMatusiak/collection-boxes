package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.boxes.CollectionBoxesStateDTO;
import com.sii.collection_boxes.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping("/assignBox/{boxID}")
    public ResponseEntity<String> assignBox(@PathVariable Long boxID,
            @RequestParam Long eventID){
        collectionBoxService.assignBox(boxID, eventID);
        String msg = String.format("Box with ID %d assigned successfully to event %d", boxID, eventID);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/addMoney/{boxID}")
    public ResponseEntity<String> addMoney(@PathVariable Long boxID,
                                           @RequestParam BigDecimal amount,
                                           @RequestParam String currency){
        collectionBoxService.addMoney(boxID, amount, currency);
        String msg = String.format("Money added to box with ID %d", boxID);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/emptyBox/{boxID}")
    public ResponseEntity<String> emptyBox(@PathVariable Long boxID){
        collectionBoxService.emptyBox(boxID);
        String msg = "Money transferred to event";
        return ResponseEntity.ok(msg);
    }
}
