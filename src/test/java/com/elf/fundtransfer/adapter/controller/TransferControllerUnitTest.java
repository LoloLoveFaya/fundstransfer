package com.elf.fundtransfer.adapter.controller;

import com.elf.fundtransfer.domain.exception.InsufficientBalanceException;
import com.elf.fundtransfer.domain.model.Transfer;
import com.elf.fundtransfer.domain.service.TransferService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class TransferControllerUnitTest {
    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void transferFunds_Success() {
        Transfer transfer = new Transfer();
        doNothing().when(transferService).transferFunds(any(Transfer.class));

        ResponseEntity<String> response = transferController.transferFunds(transfer);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transfer successful", response.getBody());
    }

    @Test
    public void transferFunds_InsufficientBalance() {
        Transfer transfer = new Transfer();
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(transferService).transferFunds(any(Transfer.class));

        ResponseEntity<String> response = transferController.transferFunds(transfer);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Insufficient balance", response.getBody());
    }
}
