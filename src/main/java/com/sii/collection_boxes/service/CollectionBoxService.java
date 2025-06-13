package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.boxes.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionBoxService {

    CollectionBoxRepository collectionBoxRepository;

    public CollectionBoxService(CollectionBoxRepository collectionBoxRepository){
        this.collectionBoxRepository = collectionBoxRepository;
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

    public void unregisterBox(Long id){
        CollectionBox box = collectionBoxRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No box with ID " + id));
        box.setAmount(BigDecimal.ZERO);
        box.setEmpty(true);
        collectionBoxRepository.save(box);
    }
}
