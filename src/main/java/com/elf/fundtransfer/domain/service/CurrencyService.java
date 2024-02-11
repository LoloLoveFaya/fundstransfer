package com.elf.fundtransfer.domain.service;

public interface CurrencyService {
    double convert(double amount, String fromCurrency, String toCurrency);
}
