package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.exceptions.NoSuchBoxException;
import com.sii.collection_boxes.exceptions.NoSuchEventException;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void unregisterBox_deletesByID(){
        when(boxRepository.existsById(6L)).thenReturn(true);
        collectionBoxService.unregisterBox(6L);
        verify(boxRepository).deleteById(6L);
    }

    @Test
    void unregisterBox_throws404whenNotFound() {
        when(boxRepository.existsById(42L)).thenReturn(false);
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.unregisterBox(42L)
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 42");
        verify(boxRepository, never()).deleteById(any());
    }

}
