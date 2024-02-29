package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.exception.AccountAlreadyExistsException;
import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.service.AccountService;
import com.elf.fundtransfer.domain.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountJpaRepository accountJpaRepository;

    @Autowired
    public AccountServiceImpl(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public void createAccount(Account account) {
        if(account.getOwnerId() == null){
            throw new AccountAlreadyExistsException("Account ownerId can't be null");
        }
        if(existsByOwnerId(account.getOwnerId())){
            throw new AccountAlreadyExistsException("An account with ownerId " + account.getOwnerId() + " already exists");
        }
        if (account.getOwnerId() != 0L) {
            accountJpaRepository.save(account);
        } else {
            throw new AccountAlreadyExistsException("Account ownerId cannot be empty");
        }
    }

    @Override
    public Optional<Account> findByOwnerId(Long ownerId) {
            return accountJpaRepository.findByOwnerId(ownerId);
    }

    @Override
    public boolean existsByOwnerId(Long ownerId){
        return accountJpaRepository.existsByOwnerId(ownerId);
    }

    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountJpaRepository.findAll(pageable);
    }

}
