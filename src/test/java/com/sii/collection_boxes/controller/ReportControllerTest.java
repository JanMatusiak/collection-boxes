package com.sii.collection_boxes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.sii.collection_boxes.dto.FinancialReportDTO;
import com.sii.collection_boxes.service.EventService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    void generateReport_returnsEvents() throws Exception {
        // given
        List<FinancialReportDTO> reports = List.of(
                new FinancialReportDTO("CharityRun", BigDecimal.valueOf(123.45), "EUR"),
                new FinancialReportDTO("FoodDrive",  BigDecimal.valueOf(50), "USD")
        );
        when(eventService.displayReport()).thenReturn(reports);

        mockMvc.perform(get("/generateReport")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("CharityRun"))
                .andExpect(jsonPath("$[0].balance").value(123.45))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[1].eventName").value("FoodDrive"))
                .andExpect(jsonPath("$[1].balance").value(50.0))
                .andExpect(jsonPath("$[1].currency").value("USD"));

        verify(eventService).displayReport();
        verifyNoMoreInteractions(eventService);
    }
}
