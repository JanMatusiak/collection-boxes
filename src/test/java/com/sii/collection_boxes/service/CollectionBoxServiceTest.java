package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.exceptions.*;
import com.sii.collection_boxes.repository.CollectionBoxRepository;
import com.sii.collection_boxes.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
        when(boxRepository.existsById(7L)).thenReturn(false);
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.unregisterBox(7L)
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).deleteById(any());
    }

    @Test
    void assignBox_setsEvent(){
        CollectionBox box = new CollectionBox();
        box.setId(5L);
        when(boxRepository.findById(5L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(5L, "Redcross");
        assertThat(box.getEvent()).isSameAs(event);
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void assignBox_throws404whenBoxNotFound(){
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenEventNotFound(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.empty());
        NoSuchEventException ex = assertThrows(
                NoSuchEventException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no event named Redcross");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenAlreadyAssigned(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        box.setEvent(new Event("Caritas","PLN"));
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross"))
                .thenReturn(Optional.of(new Event("Redcross","EUR")));
        BoxAlreadyAssignedException ex = assertThrows(
                BoxAlreadyAssignedException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is already assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenBoxNotEmpty(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        box.addAmount(BigDecimal.valueOf(100), "USD");
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross"))
                .thenReturn(Optional.of(new Event("Redcross","EUR")));
        BoxNotEmptyException ex = assertThrows(
                BoxNotEmptyException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not empty");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_updatesBalance(){
        CollectionBox box = new CollectionBox();
        box.setId(5L);
        when(boxRepository.findById(5L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(5L, "Redcross");
        clearInvocations(boxRepository);
        collectionBoxService.addMoney(5L, BigDecimal.valueOf(100), "EUR");
        assertThat(box.getBalance("EUR")).isEqualTo(BigDecimal.valueOf(100));
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void addMoney_throws404whenBoxNotFound(){
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenBoxUnassigned() {
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        UnassignedBoxException ex = assertThrows(
                UnassignedBoxException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenUnsupportedCurrency(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository);
        UnsupportedCurrencyException ex = assertThrows(
                UnsupportedCurrencyException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "GBP")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Unsupported currency: GBP");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenNonpositiveAmount(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository);
        NonpositiveAmountException ex = assertThrows(
                NonpositiveAmountException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(-100), "EUR")
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("The value must be positive");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_transfersFunds(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR");
        clearInvocations(boxRepository, eventRepository);
        collectionBoxService.emptyBox(7L);
        assertThat(event.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(box.isEmpty()).isTrue();
        verify(boxRepository).save(any(CollectionBox.class));
        verify(eventRepository).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxNotFound(){
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.emptyBox(7L));
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxUnassigned(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        UnassignedBoxException ex = assertThrows(
                UnassignedBoxException.class,
                () -> collectionBoxService.emptyBox(7L));
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxAlreadyEmpty(){
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository, eventRepository);
        BoxAlreadyEmptyException ex = assertThrows(
                BoxAlreadyEmptyException.class,
                () -> collectionBoxService.emptyBox(7L));
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is already empty");
        verify(boxRepository, never()).save(any());
    }
}
