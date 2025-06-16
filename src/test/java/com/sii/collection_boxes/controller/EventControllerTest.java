package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CreateEventDTO;
import com.sii.collection_boxes.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    void createEvent_returnsMessage () throws Exception {
        doNothing().when(eventService).createEvent(any(CreateEventDTO.class));
        mockMvc.perform(post("/createEvent")
                        .param("name", "Redcross")
                        .param("currency", "EUR"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Event Redcross created successfully"));
        verify(eventService).createEvent(any(CreateEventDTO.class));
    }
}
