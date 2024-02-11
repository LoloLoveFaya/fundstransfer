package com.elf.fundtransfer.domain.repository;

import com.elf.fundtransfer.domain.model.Account;

public interface AccountRepository {
    void save(Account account);

    Account findById(Long ownerId);
}
