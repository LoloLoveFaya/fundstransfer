package com.elf.fundtransfer.adapter.controller;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<Void> createAccount(@RequestBody Account account) {
        accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/accounts/{ownerId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long ownerId) {
        Optional<Account> accountOptional = accountService.findByOwnerId(ownerId);
        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/accounts")
    public ResponseEntity<Page<Account>> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountService.getAllAccounts(pageable);
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(accounts);
        }
    }
}
