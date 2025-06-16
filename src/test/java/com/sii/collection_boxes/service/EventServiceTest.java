package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.entity.CollectionBox;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.exceptions.EventNameTakenException;
import com.sii.collection_boxes.exceptions.UnsupportedCurrencyException;
import com.sii.collection_boxes.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventService eventService;

    @Test
    void createEvent_savesNewEvent() {
        CreateEventDTO dto = new CreateEventDTO("Redcross", "EUR");
        when(eventRepository.existsByName("Redcross")).thenReturn(false);
        eventService.createEvent(dto);

        verify(eventRepository).save(argThat(event ->
                "Redcross".equals(event.getName()) &&
                        "EUR".equals(event.getCurrency()) &&
                        event.getBalance().compareTo(BigDecimal.ZERO) == 0
        ));
    }

    @Test
    void createEvent_throws404whenNameTaken(){
        when(eventRepository.existsByName("Redcross")).thenReturn(true);
        EventNameTakenException ex = assertThrows(
                EventNameTakenException.class,
                () -> eventService.createEvent(new CreateEventDTO("Redcross", "EUR"))
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Name Redcross is already taken");
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createEvent_throws404whenUnsupportedCurrency(){
        when(eventRepository.existsByName("Redcross")).thenReturn(false);
        UnsupportedCurrencyException ex = assertThrows(
                UnsupportedCurrencyException.class,
                () -> eventService.createEvent(new CreateEventDTO("Redcross", "GBP"))
        );
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Unsupported currency: GBP");
        verify(eventRepository, never()).save(any());
    }
}
