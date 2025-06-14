package com.sii.collection_boxes.dto;

import java.math.BigDecimal;

public class FinancialReportDTO {
    private String eventName;
    private BigDecimal balance;
    private String currency;

    public FinancialReportDTO(String eventName, BigDecimal balance, String currency){
        this.eventName = eventName;
        this.balance = balance;
        this.currency = currency;
    }

    public String getEventName() {
        return eventName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
