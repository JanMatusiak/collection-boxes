package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.entity.SupportedCurrencies;
import com.sii.collection_boxes.exceptions.*;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final EventRepository eventRepository;
    private final ConversionService conversionService;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository,
                                EventRepository eventRepository, ConversionService conversionService){
        this.collectionBoxRepository = collectionBoxRepository;
        this.eventRepository = eventRepository;
        this.conversionService = conversionService;
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
            throw new NoSuchBoxException(id);
        }
        collectionBoxRepository.deleteById(id);
    }

    @Transactional
    public void assignBox(Long boxID, String eventName){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new NoSuchBoxException(boxID));

        Event event = eventRepository.findByName(eventName)
                .orElseThrow(() -> new NoSuchEventException(eventName));

        if(box.isAssigned()) throw new BoxAlreadyAssignedException(boxID);
        if(!box.isEmpty()) throw new BoxNotEmptyException(boxID);

        box.setEvent(event);
        collectionBoxRepository.save(box);
    }

    @Transactional
    public void addMoney(Long boxID, BigDecimal amount, String currency){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new NoSuchBoxException(boxID));
        if (!box.isAssigned()) throw new UnassignedBoxException(boxID);
        if (!EnumUtils.isValidEnum(SupportedCurrencies.class, currency))
            throw new UnsupportedCurrencyException(currency);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NonpositiveAmountException();
        box.addAmount(amount, currency);
        collectionBoxRepository.save(box);
    }

    @Transactional
    public void emptyBox(Long boxID){
        CollectionBox box = collectionBoxRepository.findById(boxID)
                .orElseThrow(() -> new NoSuchBoxException(boxID));
        Event event = box.getEvent();
        if (event == null) throw new UnassignedBoxException(boxID);
        if (box.isEmpty()) throw new BoxAlreadyEmptyException(boxID);

        String targetCurrency = event.getCurrency();
        BigDecimal toTransfer = BigDecimal.ZERO;

        for (var entry : box.getBalance().entrySet()) {
            String boxCurrency = entry.getKey();
            BigDecimal boxAmount = entry.getValue();

            if (boxCurrency.equals(targetCurrency)) {
                toTransfer = toTransfer.add(boxAmount);
            } else {
                BigDecimal rate = conversionService.getRate(boxCurrency, targetCurrency);
                toTransfer = toTransfer.add(boxAmount.multiply(rate));
            }
        }

        event.addToBalance(toTransfer);
        box.clearBalance();
        collectionBoxRepository.save(box);
        eventRepository.save(event);
    }
}
