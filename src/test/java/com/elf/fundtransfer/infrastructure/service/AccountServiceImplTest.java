package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.model.valueobject.Currency;
import com.elf.fundtransfer.domain.repository.AccountJpaRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountJpaRepository accountJpaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountJpaRepository);
    }

    @Test
    public void createAccount_Success() {
        Account account = new Account(1L, 1L, Currency.USD, 100.0);
        accountService.createAccount(account);

        verify(accountJpaRepository, times(1)).save(account);
    }

    @Test
    public void findById_Success() {
        Account expectedAccount = new Account(1L,1L, Currency.USD, 100.0);
        when(accountJpaRepository.findById(1L)).thenReturn(Optional.of(expectedAccount));
        Optional<Account> actualAccount = accountService.findByOwnerId(1L);

        assertEquals(expectedAccount, actualAccount);
    }
}
