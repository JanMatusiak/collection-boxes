package com.sii.collection_boxes.service;

import com.sii.collection_boxes.dto.RatesResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class LiveCurrencyConversionServiceTest {

    @Mock
    WebClient.Builder builder;

    @Mock
    WebClient client;

    @Mock
    WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    LiveConversionService liveConversionService;

    @BeforeEach
    void setUp(){
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(client);

        doReturn(uriSpec).when(client).get();
        doReturn(headersSpec).when(uriSpec).uri("/v6/{key}/pair/{from}/{to}", "TEST_KEY", "EUR", "USD");

        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RatesResponseDTO.class))
                .thenReturn(Mono.just(new RatesResponseDTO("success", BigDecimal.TEN)));

        liveConversionService = new LiveConversionService(builder, "TEST_KEY");
    }

    @Test
    void getRate_shouldInvokeWebClientChain(){
        // given + when
        BigDecimal rate = liveConversionService.getRate("EUR", "USD");
        // then
        verify(uriSpec).uri("/v6/{key}/pair/{from}/{to}", "TEST_KEY", "EUR", "USD");
        verify(headersSpec).retrieve();
        assertThat(rate).isEqualByComparingTo(BigDecimal.TEN);
    }

}
