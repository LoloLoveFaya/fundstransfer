package com.elf.fundtransfer.infrastructure.repository;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.model.valueobject.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountRepositoryImplTest {

    @InjectMocks
    private AccountRepositoryImpl accountRepository;

    @Test
    public void saveAndFindById_Success() {
        Account account = new Account(1L, Currency.USD, 100.0);
        accountRepository.save(account);
        Account retrievedAccount = accountRepository.findById(1L);

        assertEquals(account, retrievedAccount);
    }

    @Test
    public void findById_NonExistentId_ReturnsNull() {
        Account retrievedAccount = accountRepository.findById(999L);

        assertNull(retrievedAccount);
    }
}
