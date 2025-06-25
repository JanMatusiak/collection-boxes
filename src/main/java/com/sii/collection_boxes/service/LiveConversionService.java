package com.sii.collection_boxes.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sii.collection_boxes.dto.RatesResponseDTO;
import com.sii.collection_boxes.exceptions.UnsupportedConversionException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
@Profile("live-rates")
public class LiveConversionService implements ConversionService {
    private final WebClient client;
    private final String apiKey;

    public LiveConversionService(WebClient.@NotNull Builder builder,
                                 @Value("${exchangerate.api-key}") String apiKey) {
        this.client = builder.baseUrl("https://v6.exchangerate-api.com").build();
        this.apiKey = apiKey;
    }

    public record PairResponse(
            @JsonProperty("result") String result,
            @JsonProperty("conversion_rate") BigDecimal conversionRate
    ) {}

    @Override
    public BigDecimal getRate(@NotNull String from, String to) {
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }

        PairResponse pairResponse = client.get()
                .uri("/v6/{key}/pair/{from}/{to}", apiKey, from, to)
                .retrieve()
                .bodyToMono(PairResponse.class)
                .block();

        if (!"success".equals(pairResponse.result())) {
            String pair = from + '_' + to;
            throw new UnsupportedConversionException(pair);
        }
        return pairResponse.conversionRate();
    }
}
