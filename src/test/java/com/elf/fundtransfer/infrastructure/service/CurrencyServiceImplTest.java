package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.model.valueobject.Currency;
import com.elf.fundtransfer.infrastructure.exception.ExchangeRateConversionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceImplTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    public void testConvert_Success() {
        String mockApiResponse = "{\"conversion_rates\":{\"EUR\":0.85,\"GBP\":0.75,\"AUD\":1.5},\"result\":\"success\"}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockApiResponse);

        double convertedAmount = currencyService.convert(
                100,
                Currency.USD.name(),
                Currency.EUR.name());
        assertEquals(85.0, convertedAmount);
    }


    @Test
    public void testConvert_FailedApiResponse() {
        String mockApiResponse = "{\"result\":\"error\",\"error-type\":\"unknown-code\"}";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(mockApiResponse);

        assertThrows(ExchangeRateConversionException.class, () ->
                currencyService.convert(
                        100,
                        Currency.USD.name(),
                        Currency.EUR.name()));
    }
}
