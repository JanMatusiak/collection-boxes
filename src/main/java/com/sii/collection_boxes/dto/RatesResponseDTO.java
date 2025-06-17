package com.sii.collection_boxes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RatesResponseDTO(String result, BigDecimal conversionRate) {
    public RatesResponseDTO(
            String result,
            @JsonProperty("conversion_rate") BigDecimal conversionRate
    ) {
        this.result = result;
        this.conversionRate = conversionRate;
    }
}
