package com.elf.fundtransfer.domain.service;

import com.elf.fundtransfer.domain.model.Account;

import java.util.Optional;

public interface AccountService {
    void createAccount(Account account);

    Optional<Account> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);

}
