package com.elf.fundtransfer.domain.service;

import com.elf.fundtransfer.domain.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransferService {
    void transferFunds(Transfer transfer);
    Page<Transfer> getTransferHistory(Pageable pageable);
}
