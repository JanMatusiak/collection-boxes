package com.sii.collection_boxes.service;

import com.sii.collection_boxes.exceptions.UnsupportedConversionException;
import com.sii.collection_boxes.utility.ExchangeConversion;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConstantCurrencyConversionServiceTest {

    @Spy
    ConversionService conversionService = new ConstantConversionService();

    static @NotNull Stream<Arguments> currencyPairs() {
        return Stream.of(
                Arguments.of("PLN", "EUR", ExchangeConversion.PLN_TO_EUR),
                Arguments.of("PLN", "USD", ExchangeConversion.PLN_TO_USD),
                Arguments.of("EUR", "PLN", ExchangeConversion.EUR_TO_PLN),
                Arguments.of("EUR", "USD", ExchangeConversion.EUR_TO_USD),
                Arguments.of("USD", "PLN", ExchangeConversion.USD_TO_PLN),
                Arguments.of("USD", "EUR", ExchangeConversion.USD_TO_EUR),
                Arguments.of("PLN", "PLN", BigDecimal.ONE),
                Arguments.of("EUR", "EUR", BigDecimal.ONE),
                Arguments.of("USD", "USD", BigDecimal.ONE)
        );
    }

    @ParameterizedTest(name = "{0}→{1} @ rate={2}")
    @MethodSource("currencyPairs")
    void convert_variousRatesProduceCorrectResult(String from, String to,
                                                  BigDecimal expectedRate) {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal expected = amount.multiply(expectedRate);
        // when
        BigDecimal actual = amount.multiply(conversionService.getRate(from, to));
        // then
        assertThat(actual)
                .as("100 * %s→%s rate", from, to)
                .isEqualByComparingTo(expected);
    }

    @Test
    void convert_invalidCurrencyPair() {
        // given + when
        UnsupportedConversionException ex = assertThrows(
                UnsupportedConversionException.class,
                () -> conversionService.getRate("GBP", "SVK")
        );
        // then
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getReason()).isEqualTo("Unsupported currency conversion: GBP_SVK");
    }
}
