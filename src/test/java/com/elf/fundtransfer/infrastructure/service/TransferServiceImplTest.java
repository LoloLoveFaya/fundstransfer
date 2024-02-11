package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.exception.InsufficientBalanceException;
import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.model.Transfer;
import com.elf.fundtransfer.domain.model.valueobject.Currency;
import com.elf.fundtransfer.domain.repository.AccountRepository;
import com.elf.fundtransfer.domain.service.CurrencyService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferFunds_SuccessfulTransfer_SameCurrency() {
        Account debitAccount = new Account(1L, Currency.USD, 100.0);
        Account creditAccount = new Account(2L, Currency.USD, 0.0);
        Transfer transfer = new Transfer(1L, 1L, 2L, 50.0, new Date(), Currency.USD);

        when(accountRepository.findById(1L)).thenReturn(debitAccount);
        when(accountRepository.findById(2L)).thenReturn(creditAccount);

        transferService.transferFunds(transfer);

        assertEquals(50.0, debitAccount.getBalance());
        assertEquals(50.0, creditAccount.getBalance());
        verify(accountRepository, times(1)).save(debitAccount);
        verify(accountRepository, times(1)).save(creditAccount);
    }

    @Test
    public void testTransferFunds_SuccessfulTransfer_DifferentCurrency() {
        Account debitAccount = new Account(1L, Currency.USD, 100.0);
        Account creditAccount = new Account(2L, Currency.EUR, 0.0);
        Transfer transfer = new Transfer(1L, 1L, 2L, 50.0, new Date(), Currency.USD);

        when(accountRepository.findById(1L)).thenReturn(debitAccount);
        when(accountRepository.findById(2L)).thenReturn(creditAccount);

        // 1 USD = 0.85 EUR, so 50.0 USD should convert to 42.5 EUR
        when(currencyService.convert(eq(50.0), eq("USD"), eq("EUR"))).thenReturn(46.36);
        transferService.transferFunds(transfer);

        assertEquals(50.0, debitAccount.getBalance());
        assertEquals(46.36, creditAccount.getBalance()); // Converted to EUR
        verify(accountRepository, times(1)).save(debitAccount);
        verify(accountRepository, times(1)).save(creditAccount);
    }

    @Test
    public void testTransferFunds_InsufficientBalance() {
        Account debitAccount = new Account(1L, Currency.USD, 0.0);
        Account creditAccount = new Account(2L, Currency.USD, 0.0);
        Transfer transfer = new Transfer(1L, 1L, 2L, 100.0, new Date(), Currency.USD);

        when(accountRepository.findById(1L)).thenReturn(debitAccount);
        when(accountRepository.findById(2L)).thenReturn(creditAccount);

        assertThrows(InsufficientBalanceException.class, () -> transferService.transferFunds(transfer));
        verify(accountRepository, never()).save(any());
    }

    @Test
    public void testTransferFunds_DebitAccountNotFound() {
        Account creditAccount = new Account(2L, Currency.USD, 0.0);
        Transfer transfer = new Transfer(1L, 1L, 2L, 50.0, new Date(), Currency.USD);

        when(accountRepository.findById(1L)).thenReturn(null);
        when(accountRepository.findById(2L)).thenReturn(creditAccount);

        assertThrows(IllegalArgumentException.class, () -> transferService.transferFunds(transfer));
        verify(accountRepository, never()).save(any());
    }

    @Test
    public void testTransferFunds_CreditAccountNotFound() {
        Account debitAccount = new Account(1L, Currency.USD, 100.0);
        Transfer transfer = new Transfer(1L, 1L, 2L, 50.0, new Date(), Currency.USD);

        when(accountRepository.findById(1L)).thenReturn(debitAccount);
        when(accountRepository.findById(2L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transferService.transferFunds(transfer));
        verify(accountRepository, never()).save(any());
    }
}
