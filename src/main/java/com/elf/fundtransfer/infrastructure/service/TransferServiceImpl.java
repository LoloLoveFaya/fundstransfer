package com.elf.fundtransfer.infrastructure.service;

import com.elf.fundtransfer.domain.exception.InsufficientBalanceException;
import com.elf.fundtransfer.domain.model.Account;
import com.elf.fundtransfer.domain.model.Transfer;
import com.elf.fundtransfer.domain.repository.AccountRepository;
import com.elf.fundtransfer.domain.service.CurrencyService;
import com.elf.fundtransfer.domain.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;

    @Autowired
    public TransferServiceImpl(AccountRepository accountRepository, CurrencyService currencyService) {
        this.accountRepository = accountRepository;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public void transferFunds(Transfer transfer) {
        if (transfer.getDebitAccountId().equals(transfer.getCreditAccountId())) {
            logger.error("Cannot transfer funds between the same account");
            throw new IllegalArgumentException("Cannot transfer funds between the same account");
        }

        Account debitAccount = accountRepository.findById(transfer.getDebitAccountId());
        Account creditAccount = accountRepository.findById(transfer.getCreditAccountId());

        if (debitAccount == null || creditAccount == null) {
            logger.error("Either the debit or credit account does not exist");
            throw new IllegalArgumentException("Either the debit or credit account does not exist");
        }

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

            accountRepository.save(debitAccount);
            accountRepository.save(creditAccount);

            logger.info("Funds transferred successfully from account {} to account {}", debitAccount.getOwnerId(), creditAccount.getOwnerId());
        } catch (InsufficientBalanceException e) {
            logger.warn("Insufficient balance for transfer from account {} to account {}", debitAccount.getOwnerId(), creditAccount.getOwnerId());
            throw new InsufficientBalanceException("Insufficient balance in the debit account");
        } catch (Exception e) {
            logger.error("Error transferring funds from account {} to account {}: {}", debitAccount.getOwnerId(), creditAccount.getOwnerId(), e.getMessage());
            throw new RuntimeException("Error transferring funds", e);
        }
    }
}
