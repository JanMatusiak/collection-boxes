package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.entity.SupportedCurrencies;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    public void assignBox(Long boxID, String eventName){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + boxID));

        Event event = eventRepository.findByName(eventName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No event named " + eventName));

        if(box.isAssigned()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box with ID " + boxID + " is already assigned");
        }

        if(!box.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box with ID " + boxID + " is not empty");
        }

        box.setEvent(event);
        collectionBoxRepository.save(box);
    }

    public void addMoney(Long boxID, BigDecimal amount, String currency){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + boxID));
        if(!box.isAssigned()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Money cannot be added to an unassigned box");
        }
        if (!EnumUtils.isValidEnum(SupportedCurrencies.class, currency)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Unsupported currency: " + currency);
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount added must be positive");
        }
        box.addAmount(amount, currency);
        collectionBoxRepository.save(box);
    }

    @Transactional
    public void emptyBox(Long boxID){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + boxID));
        Event event = box.getEvent();
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box not assigned to any event");
        }

        if(box.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box is already empty");
        }

        String currency = event.getCurrency();
        BigDecimal toTransfer = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : box.getBalance().entrySet()){
            if (entry.getKey().equals(currency)){
                toTransfer = toTransfer.add(box.getBalance(currency));
            } else {
                toTransfer = toTransfer.add(CurrencyConversionService.convert(entry.getKey(),
                        currency, box.getBalance(entry.getKey())));
            }
        }
        event.addToBalance(toTransfer);
        box.clearBalance();
        collectionBoxRepository.save(box);
        eventRepository.save(event);
    }
}
