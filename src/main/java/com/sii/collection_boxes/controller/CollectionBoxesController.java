package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.service.CollectionBoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class CollectionBoxesController {
    private final CollectionBoxService collectionBoxService;
    private static final Logger log = LoggerFactory.getLogger(CollectionBoxesController.class);

    public CollectionBoxesController(CollectionBoxService collectionBoxService){
        this.collectionBoxService = collectionBoxService;
    }

    @PostMapping("/registerBox")
    ResponseEntity<String> registerBox(){
        Long id = collectionBoxService.registerBox();
        log.info("Box registered successfully with ID {}", id);
        String msg = String.format("Box registered successfully with ID %d", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/listBoxes")
    public ResponseEntity<List<CollectionBoxesStateDTO>> listBoxes(){
        List<CollectionBoxesStateDTO> dtoList = collectionBoxService.listBoxes();
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/unregisterBox/{id}")
    public ResponseEntity<String> unregisterBox(@PathVariable Long id){
        collectionBoxService.unregisterBox(id);
        log.info("Box with ID {} unregistered successfully", id);
        String msg = String.format("Box with ID %d unregistered successfully", id);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/assignBox/{boxID}")
    public ResponseEntity<String> assignBox(@PathVariable Long boxID,
            @RequestParam String eventName){
        collectionBoxService.assignBox(boxID, eventName);
        log.info("Box with ID {} assigned successfully to event {}", boxID, eventName);
        String msg = String.format("Box with ID %d assigned successfully to event %s", boxID, eventName);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/addMoney/{boxID}")
    public ResponseEntity<String> addMoney(@PathVariable Long boxID,
                                           @RequestParam BigDecimal amount,
                                           @RequestParam String currency){
        collectionBoxService.addMoney(boxID, amount, currency);
        log.info("Added {} {} to box {}", amount, currency, boxID);
        String msg = String.format("Money added to box with ID %d", boxID);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/emptyBox/{boxID}")
    public ResponseEntity<String> emptyBox(@PathVariable Long boxID){
        collectionBoxService.emptyBox(boxID);
        log.info("Money from box {} transferred to event", boxID);
        String msg = "Money transferred to event";
        return ResponseEntity.ok(msg);
    }
}
