package com.sii.collection_boxes.dto;

import java.math.BigDecimal;

public record FinancialReportDTO(String eventName, BigDecimal balance, String currency) {
}
