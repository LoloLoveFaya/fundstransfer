package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.exception.InsufficientBalanceException;
import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.model.Transfer;
import com.elf.fundtransfer.domain.repository.AccountJpaRepository;
import com.elf.fundtransfer.domain.repository.TransferJpaRepository;
import com.elf.fundtransfer.domain.service.CurrencyService;
import com.elf.fundtransfer.domain.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
    private final AccountJpaRepository accountJpaRepository;
    private final TransferJpaRepository transferJpaRepository;
    private final CurrencyService currencyService;

    @Autowired
    public TransferServiceImpl(
            CurrencyService currencyService,
            AccountJpaRepository accountJpaRepository,
            TransferJpaRepository transferJpaRepository) {
        this.currencyService = currencyService;
        this.accountJpaRepository = accountJpaRepository;
        this.transferJpaRepository = transferJpaRepository;
    }

    @Override
    @Transactional
    public void transferFunds(Transfer transfer) {
        UUID transferId = UUID.randomUUID();
        transfer.setTransferId(transferId);
        transfer.setTimestamp(new Date());

        if (transfer.getDebitAccountId().equals(transfer.getCreditAccountId())) {
            logger.error("Cannot transfer funds between the same account");
            throw new IllegalArgumentException("Cannot transfer funds between the same account");
        }

        Optional<Account> debitAccountOptional = accountJpaRepository.findByOwnerId(transfer.getDebitAccountId());
        Account debitAccount = debitAccountOptional.orElseThrow(() -> {
            logger.error("The debit account does not exist");
            return new IllegalArgumentException("The debit account does not exist");
        });

        Account creditAccount = accountJpaRepository.findByOwnerId(transfer.getCreditAccountId())
                .orElseThrow(() -> {
                    logger.error("The credit account does not exist");
                    return new IllegalArgumentException("The credit account does not exist");
                });

        try {
            // Calculate the transfer amount in the currency of the debit account
            double transferAmountInDebitCurrency = transfer.getAmount();
            if (!transfer.getCurrency().equals(debitAccount.getCurrency())) {
                transferAmountInDebitCurrency = currencyService.convert(
                        transferAmountInDebitCurrency,
                        transfer.getCurrency().name(),
                        debitAccount.getCurrency().name());
            }

            // Calculate the transfer amount in the currency of the credit account if needed
            double transferAmountInCreditCurrency = transfer.getAmount();
            if (!transfer.getCurrency().equals(creditAccount.getCurrency())) {
                transferAmountInCreditCurrency = currencyService.convert(
                        transferAmountInCreditCurrency,
                        transfer.getCurrency().name(),
                        creditAccount.getCurrency().name());
            }

            if (debitAccount.getBalance() < transferAmountInDebitCurrency) {
                throw new InsufficientBalanceException("Insufficient balance in the debit account");
            }

            // Perform the transfer
            debitAccount.setBalance(debitAccount.getBalance() - transferAmountInDebitCurrency);
            creditAccount.setBalance(creditAccount.getBalance() + transferAmountInCreditCurrency);

            accountJpaRepository.save(debitAccount);
            accountJpaRepository.save(creditAccount);

            transferJpaRepository.save(transfer);

            logger.info("Funds transferred successfully from account {} to account {}", debitAccount.getOwnerId(), creditAccount.getOwnerId());
        } catch (InsufficientBalanceException e) {
            logger.warn("Insufficient balance for transfer from account {} to account {}", debitAccount.getOwnerId(), creditAccount.getOwnerId());
            throw new InsufficientBalanceException("Insufficient balance in the debit account");
        } catch (Exception e) {
            logger.error("Error transferring funds from account {} to account {}: {}", debitAccount.getOwnerId(), creditAccount.getOwnerId(), e.getMessage());
            throw new RuntimeException("Error transferring funds", e);
        }
    }

    @Override
    public Page<Transfer> getTransferHistory(Pageable pageable) {
        return transferJpaRepository.findAll(pageable);
    }
}
