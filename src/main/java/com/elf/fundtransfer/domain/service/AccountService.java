package com.elf.fundtransfer.domain.service;

import com.elf.fundtransfer.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountService {
    void createAccount(Account account);

    Optional<Account> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);

    Page<Account> getAllAccounts(Pageable pageable);

}
