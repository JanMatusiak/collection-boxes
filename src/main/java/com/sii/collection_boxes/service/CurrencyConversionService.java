package com.sii.collection_boxes.service;

import com.sii.collection_boxes.exceptions.UnsupportedConversionException;
import com.sii.collection_boxes.exceptions.UnsupportedCurrencyException;
import com.sii.collection_boxes.utility.ExchangeConversion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService {
    public static BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        String pair = fromCurrency + "_" + toCurrency;
        BigDecimal rate = switch (pair) {
            case "PLN_EUR" -> ExchangeConversion.PLN_TO_EUR;
            case "PLN_USD" -> ExchangeConversion.PLN_TO_USD;
            case "EUR_PLN" -> ExchangeConversion.EUR_TO_PLN;
            case "EUR_USD" -> ExchangeConversion.EUR_TO_USD;
            case "USD_PLN" -> ExchangeConversion.USD_TO_PLN;
            case "USD_EUR" -> ExchangeConversion.USD_TO_EUR;
            case "PLN_PLN", "EUR_EUR", "USD_USD" -> BigDecimal.ONE;
            default -> throw new UnsupportedConversionException(pair);
        };
        return amount.multiply(rate);
    }
}
