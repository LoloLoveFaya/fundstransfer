package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.repository.AccountRepository;
import com.elf.fundtransfer.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findById(Long ownerId) {
        Account account = accountRepository.findById(ownerId);
        return account;
    }
}
