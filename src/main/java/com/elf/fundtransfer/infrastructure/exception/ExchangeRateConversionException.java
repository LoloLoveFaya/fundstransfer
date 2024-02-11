package com.elf.fundtransfer.infrastructure.exception;

public class ExchangeRateConversionException extends RuntimeException {
    public ExchangeRateConversionException(String message) {
        super(message);
    }

    public ExchangeRateConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
