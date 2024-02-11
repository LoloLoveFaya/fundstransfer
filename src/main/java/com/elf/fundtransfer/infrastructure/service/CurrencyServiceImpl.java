package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.service.CurrencyService;
import com.elf.fundtransfer.infrastructure.exception.ExchangeRateConversionException;
import com.elf.fundtransfer.infrastructure.external.ExchangeRateApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Value("${exchange-rate-api.url}")
    private String apiUrl;
    @Value("${exchange-rate-api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double convert(double amount, String fromCurrency, String toCurrency) {
        try {
            String fullUrl = apiUrl + "?base=" + fromCurrency + "&symbols=" + toCurrency + "&access_key=" + apiKey;
            String jsonResponse = restTemplate.getForObject(fullUrl, String.class);
            ExchangeRateApiResponse response = convertJsonToApiResponse(jsonResponse);
            if (response != null && response.getConversion_rates() != null && response.getConversion_rates().containsKey(toCurrency)) {
                return amount * response.getConversion_rates().get(toCurrency);
            } else {
                throw new ExchangeRateConversionException("Unable to fetch exchange rate or invalid response");
            }
        } catch (Exception e) {
            logger.error("Error converting exchange rate", e);
            throw new ExchangeRateConversionException("Error converting exchange rate", e);
        }
    }

    private ExchangeRateApiResponse convertJsonToApiResponse(String jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonResponse, ExchangeRateApiResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing exchange rate response", e);
            throw new ExchangeRateConversionException("Error parsing exchange rate response", e);
        }
    }
}
