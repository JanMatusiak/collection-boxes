package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.entity.Event;
import com.sii.collection_boxes.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
