package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.CollectionBoxesStateDTO;
import com.sii.collection_boxes.service.CollectionBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void registerBox_returnsId() throws Exception {
        when(collectionBoxService.registerBox()).thenReturn(42L);
        mockMvc.perform(post("/registerBox"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Box registered successfully with ID 42"));
        verify(collectionBoxService).registerBox();
    }

    @Test
    void listBoxes_returnsBoxDtos() throws Exception {
        List<CollectionBoxesStateDTO> dtos = List.of(
                new CollectionBoxesStateDTO(true, false),
                new CollectionBoxesStateDTO(false, true)
        );
        when(collectionBoxService.listBoxes()).thenReturn(dtos);

        mockMvc.perform(get("/listBoxes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].empty").value(true))
                .andExpect(jsonPath("$[0].assigned").value(false))
                .andExpect(jsonPath("$[1].empty").value(false))
                .andExpect(jsonPath("$[1].assigned").value(true));

        verify(collectionBoxService).listBoxes();
    }

    @Test
    void unregisterBox_returnsId() throws Exception {
        doNothing().when(collectionBoxService).unregisterBox(7L);
        mockMvc.perform(delete("/unregisterBox/7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Box with ID 7 unregistered successfully"));
        verify(collectionBoxService).unregisterBox(7L);
    }

    @Test
    void assignBox_returnsMessage() throws Exception {
        doNothing().when(collectionBoxService).assignBox(1L, "CharityRun");
        mockMvc.perform(put("/assignBox/1")
                        .param("eventName", "CharityRun"))
                .andExpect(status().isOk())
                .andExpect(content().string("Box with ID 1 assigned successfully to event CharityRun"));
        verify(collectionBoxService).assignBox(1L, "CharityRun");
    }

    @Test
    void addMoney_returnsMessage() throws Exception {
        doNothing().when(collectionBoxService).addMoney(2L, BigDecimal.valueOf(50), "EUR");
        mockMvc.perform(put("/addMoney/2")
                        .param("amount", "50")
                        .param("currency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(content().string("Money added to box with ID 2"));
        verify(collectionBoxService).addMoney(2L, BigDecimal.valueOf(50), "EUR");
    }

    @Test
    void emptyBox_returnsMessage() throws Exception {
        doNothing().when(collectionBoxService).emptyBox(3L);
        mockMvc.perform(put("/emptyBox/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Money transferred to event"));
        verify(collectionBoxService).emptyBox(3L);
    }
}
