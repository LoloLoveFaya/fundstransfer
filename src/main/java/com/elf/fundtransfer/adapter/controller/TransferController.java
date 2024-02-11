package com.elf.fundtransfer.adapter.controller;

import com.elf.fundtransfer.domain.exception.InsufficientBalanceException;
import com.elf.fundtransfer.domain.model.Transfer;
import com.elf.fundtransfer.domain.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfers")
    public ResponseEntity<String> transferFunds(@RequestBody Transfer transfer) {
        try {
            transferService.transferFunds(transfer);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException | InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}

