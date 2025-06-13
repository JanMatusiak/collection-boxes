package com.sii.collection_boxes.service;

import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import org.springframework.stereotype.Service;

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
}
