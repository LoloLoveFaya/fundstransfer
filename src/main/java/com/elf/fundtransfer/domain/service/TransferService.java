package com.elf.fundtransfer.domain.service;

import com.elf.fundtransfer.domain.model.Transfer;

public interface TransferService {
    void transferFunds(Transfer transfer);
}
