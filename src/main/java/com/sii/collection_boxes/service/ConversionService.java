package com.sii.collection_boxes.service;

import java.math.BigDecimal;

public interface ConversionService {
    BigDecimal getRate(String fromCurrency, String toCurrency);
}
