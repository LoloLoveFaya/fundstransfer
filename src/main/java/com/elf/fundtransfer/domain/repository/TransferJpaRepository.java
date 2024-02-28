package com.elf.fundtransfer.domain.repository;

import com.elf.fundtransfer.domain.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferJpaRepository extends JpaRepository<Transfer, Long> {
}
