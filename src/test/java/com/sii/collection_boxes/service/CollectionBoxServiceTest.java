package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionBoxServiceTest {

    @Mock
    CollectionBoxRepository boxRepository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    CollectionBoxService collectionBoxService;

    @Test
    void registerBox_saveAndReturnID(){
        CollectionBox saved = new CollectionBox();
        saved.setId(5L);
        when(boxRepository.save(any())).thenReturn(saved);

        Long result = collectionBoxService.registerBox();

        assertThat(result).isEqualTo(5L);
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void listBoxes_findAndReturnDTOList(){
        CollectionBox box1 = new CollectionBox();
        box1.setId(1L);
        CollectionBox box2 = new CollectionBox();
        box2.setId(2L);
        Event event = new Event("Redcross", "EUR");
        box2.setEvent(event);
        box2.addAmount(BigDecimal.valueOf(1000), "EUR");

        when(boxRepository.findAll()).thenReturn(List.of(box1, box2));

        List<CollectionBoxesStateDTO> dtos = collectionBoxService.listBoxes();

        assertEquals(2, dtos.size());

        var first = dtos.get(0);
        assertThat(first.empty()).isTrue();
        assertThat(first.assigned()).isFalse();

        var second = dtos.get(1);
        assertThat(second.empty()).isFalse();
        assertThat(second.assigned()).isTrue();

        verify(boxRepository).findAll();
    }
}
