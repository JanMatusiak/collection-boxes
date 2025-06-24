package com.sii.collection_boxes.controller;

import com.sii.collection_boxes.dto.FinancialReportDTO;
import com.sii.collection_boxes.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final EventService eventService;

    public ReportController(EventService eventService){
        this.eventService = eventService;
    }

    @GetMapping
    ResponseEntity<List<FinancialReportDTO>> displayReport(){
        List<FinancialReportDTO> report = eventService.displayReport();
        return ResponseEntity.ok(report);
    }
}
