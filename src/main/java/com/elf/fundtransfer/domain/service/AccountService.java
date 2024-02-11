package com.elf.fundtransfer.domain.service;

import com.elf.fundtransfer.domain.model.Account;

public interface AccountService {
    void createAccount(Account account);

    Account findById(Long ownerId);
}
