package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.boxes.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CollectionBoxService {

    CollectionBoxRepository collectionBoxRepository;
    EventRepository eventRepository;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository,
                                EventRepository eventRepository){
        this.collectionBoxRepository = collectionBoxRepository;
        this.eventRepository = eventRepository;
    }

    public Long registerBox(){
        CollectionBox box = new CollectionBox();
        CollectionBox saved = collectionBoxRepository.save(box);
        return saved.getId();
    }

    public List<CollectionBoxesStateDTO> listBoxes(){
        List<CollectionBox> boxes = collectionBoxRepository.findAll();
        return CollectionBoxesStateDTO.toDTO(boxes);
    }

    public void unregisterBox(Long id) {
        if (!collectionBoxRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No box with ID " + id);
        }
        collectionBoxRepository.deleteById(id);
    }


    public void assignBox(Long boxID, Long eventID){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + boxID));

        Event event = eventRepository.findById(eventID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No event with ID " + eventID));

        box.setEvent(event);
        collectionBoxRepository.save(box);
    }

    public void addMoney(Long boxID, BigDecimal amount, String currency){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + boxID));
        box.addAmount(amount, currency);
        collectionBoxRepository.save(box);
    }
}
