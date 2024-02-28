package com.elf.fundtransfer.domain.repository;

import com.elf.fundtransfer.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    boolean existsByOwnerId(Long ownerId);
    Optional<Account> findByOwnerId(Long ownerId);

}
