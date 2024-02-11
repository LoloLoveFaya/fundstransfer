package com.elf.fundtransfer.adapter.controller;

import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Void> createAccount(@RequestBody Account account) {
        accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/accounts/{ownerId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long ownerId) {
        Account account = accountService.findById(ownerId);
        return ResponseEntity.ok(account);
    }
}
