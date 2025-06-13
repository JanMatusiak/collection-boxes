package com.sii.collection_boxes.service;

import com.sii.collection_boxes.utility.ExchangeConversion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CurrencyConversionService {
    public static BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount){
        if(Objects.equals(fromCurrency, "PLN") && Objects.equals(toCurrency, "EUR"))
            return amount.multiply(ExchangeConversion.PLN_TO_EUR);
        else if(Objects.equals(fromCurrency, "EUR") && Objects.equals(toCurrency, "PLN"))
            return amount.multiply(ExchangeConversion.EUR_TO_PLN);
        else if(Objects.equals(fromCurrency, "PLN") && Objects.equals(toCurrency, "USD"))
            return amount.multiply(ExchangeConversion.PLN_TO_USD);
        else if(Objects.equals(fromCurrency, "USD") && Objects.equals(toCurrency, "PLN"))
            return amount.multiply(ExchangeConversion.USD_TO_PLN);
        else  if(Objects.equals(fromCurrency, "USD") && Objects.equals(toCurrency, "EUR"))
            return amount.multiply(ExchangeConversion.USD_TO_EUR);
        else if(Objects.equals(fromCurrency, "EUR") && Objects.equals(toCurrency, "USD"))
            return amount.multiply(ExchangeConversion.EUR_TO_USD);
        else if(Objects.equals(fromCurrency, toCurrency))
            return amount.multiply(BigDecimal.ONE);
        else
            throw new IllegalArgumentException("An invalid currency");
    }
}
