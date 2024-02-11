package com.elf.fundtransfer.infrastructure.repository;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountRepositoryImpl implements AccountRepository {
    private final Map<Long, Account> accounts = new HashMap<>();

    @Override
    public void save(Account account) {
        accounts.put(account.getOwnerId(), account);
    }

    @Override
    public Account findById(Long ownerId) {
        return accounts.get(ownerId);
    }
}
