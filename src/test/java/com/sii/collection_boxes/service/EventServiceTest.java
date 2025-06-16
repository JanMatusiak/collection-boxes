package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.dto.FinancialReportDTO;
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

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.List;
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

    @Test
    void displayReport_mapEventsToDtos(){
        Event event1 = new Event("CharityRun", "EUR");
        event1.addToBalance(BigDecimal.valueOf(123.45));
        Event event2 = new Event("FoodDrive", "USD");
        event2.addToBalance(BigDecimal.valueOf(50));

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        List<FinancialReportDTO> report = eventService.displayReport();

        assertEquals(2, report.size(), "should return two entries");

        FinancialReportDTO first = report.get(0);
        assertEquals("CharityRun", first.eventName());
        assertEquals(BigDecimal.valueOf(123.45), first.balance());
        assertEquals("EUR", first.currency());

        FinancialReportDTO second = report.get(1);
        assertEquals("FoodDrive", second.eventName());
        assertEquals(BigDecimal.valueOf(50), second.balance());
        assertEquals("USD", second.currency());

        verify(eventRepository).findAll();
        verifyNoMoreInteractions(eventRepository);
    }
}
