package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.service.CollectionBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CollectionBoxControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CollectionBoxService collectionBoxService;

    @InjectMocks
    private CollectionBoxesController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void registerBox_returnsCreatedAndMessage() throws Exception {
        when(collectionBoxService.registerBox()).thenReturn(42L);

        mockMvc.perform(post("/registerBox"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Box registered successfully with ID 42"));

        verify(collectionBoxService).registerBox();
    }
}
