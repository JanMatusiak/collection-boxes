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
import org.mockito.Spy;
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

    @Spy
    ConversionService conversionService = new ConstantConversionService();

    @InjectMocks
    CollectionBoxService collectionBoxService;

    @Test
    void registerBox_saveAndReturnID(){
        //given
        CollectionBox saved = new CollectionBox();
        saved.setId(5L);
        when(boxRepository.save(any())).thenReturn(saved);
        // when
        Long result = collectionBoxService.registerBox();
        // then
        assertThat(result).isEqualTo(5L);
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void listBoxes_findAndReturnDTOList(){
        // given
        CollectionBox box1 = new CollectionBox();
        box1.setId(1L);
        CollectionBox box2 = new CollectionBox();
        box2.setId(2L);
        Event event = new Event("Redcross", "EUR");
        box2.setEvent(event);
        box2.addAmount(BigDecimal.valueOf(1000), "EUR");
        when(boxRepository.findAll()).thenReturn(List.of(box1, box2));

        // when
        List<CollectionBoxesStateDTO> dtos = collectionBoxService.listBoxes();

        // then
        assertEquals(2, dtos.size());
        var first = dtos.getFirst();
        assertThat(first.empty()).isTrue();
        assertThat(first.assigned()).isFalse();

        var second = dtos.get(1);
        assertThat(second.empty()).isFalse();
        assertThat(second.assigned()).isTrue();

        verify(boxRepository).findAll();
    }

    @Test
    void unregisterBox_deletesByID(){
        // given
        when(boxRepository.existsById(6L)).thenReturn(true);
        // when
        collectionBoxService.unregisterBox(6L);
        // then
        verify(boxRepository).deleteById(6L);
    }

    @Test
    void unregisterBox_throws404whenNotFound() {
        // given
        when(boxRepository.existsById(7L)).thenReturn(false);
        // when
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.unregisterBox(7L)
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).deleteById(any());
    }

    @Test
    void assignBox_setsEvent(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(5L);
        when(boxRepository.findById(5L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        // when
        collectionBoxService.assignBox(5L, "Redcross");
        // then
        assertThat(box.getEvent()).isSameAs(event);
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void assignBox_throws404whenBoxNotFound(){
        // given
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        // when
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenEventNotFound(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.empty());
        // when
        NoSuchEventException ex = assertThrows(
                NoSuchEventException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no event named Redcross");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenAlreadyAssigned(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        box.setEvent(new Event("Caritas","PLN"));
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross"))
                .thenReturn(Optional.of(new Event("Redcross","EUR")));
        // when
        BoxAlreadyAssignedException ex = assertThrows(
                BoxAlreadyAssignedException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is already assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void assignBox_throws404whenBoxNotEmpty(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        box.addAmount(BigDecimal.valueOf(100), "USD");
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        when(eventRepository.findByName("Redcross"))
                .thenReturn(Optional.of(new Event("Redcross","EUR")));
        // when
        BoxNotEmptyException ex = assertThrows(
                BoxNotEmptyException.class,
                () -> collectionBoxService.assignBox(7L, "Redcross")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not empty");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_updatesBalance(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(5L);
        when(boxRepository.findById(5L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(5L, "Redcross");
        clearInvocations(boxRepository);
        // when
        collectionBoxService.addMoney(5L, BigDecimal.valueOf(100), "EUR");
        // then
        assertThat(box.getBalance("EUR")).isEqualTo(BigDecimal.valueOf(100));
        verify(boxRepository).save(any(CollectionBox.class));
    }

    @Test
    void addMoney_throws404whenBoxNotFound(){
        // given
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        // when
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenBoxUnassigned() {
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        // when
        UnassignedBoxException ex = assertThrows(
                UnassignedBoxException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenUnsupportedCurrency(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository);
        // when
        UnsupportedCurrencyException ex = assertThrows(
                UnsupportedCurrencyException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "GBP")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Unsupported currency: GBP");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void addMoney_throws404whenNonpositiveAmount(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository);
        // when
        NonpositiveAmountException ex = assertThrows(
                NonpositiveAmountException.class,
                () -> collectionBoxService.addMoney(7L, BigDecimal.valueOf(-100), "EUR")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("The value must be positive");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_transfersFunds(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        collectionBoxService.addMoney(7L, BigDecimal.valueOf(100), "EUR");
        clearInvocations(boxRepository, eventRepository);
        // when
        collectionBoxService.emptyBox(7L);
        // then
        assertThat(event.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(box.isEmpty()).isTrue();
        verify(boxRepository).save(any(CollectionBox.class));
        verify(eventRepository).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxNotFound(){
        // given
        when(boxRepository.findById(7L)).thenReturn(Optional.empty());
        // when
        NoSuchBoxException ex = assertThrows(
                NoSuchBoxException.class,
                () -> collectionBoxService.emptyBox(7L));
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("There is no box with ID 7");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxUnassigned(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        // when
        UnassignedBoxException ex = assertThrows(
                UnassignedBoxException.class,
                () -> collectionBoxService.emptyBox(7L));
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is not assigned");
        verify(boxRepository, never()).save(any());
    }

    @Test
    void emptyBox_throws404whenBoxAlreadyEmpty(){
        // given
        CollectionBox box = new CollectionBox();
        box.setId(7L);
        when(boxRepository.findById(7L)).thenReturn(Optional.of(box));
        Event event = new Event("Redcross", "EUR");
        when(eventRepository.findByName("Redcross")).thenReturn(Optional.of(event));
        collectionBoxService.assignBox(7L, "Redcross");
        clearInvocations(boxRepository, eventRepository);
        // when
        BoxAlreadyEmptyException ex = assertThrows(
                BoxAlreadyEmptyException.class,
                () -> collectionBoxService.emptyBox(7L));
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Box 7 is already empty");
        verify(boxRepository, never()).save(any());
    }
}
