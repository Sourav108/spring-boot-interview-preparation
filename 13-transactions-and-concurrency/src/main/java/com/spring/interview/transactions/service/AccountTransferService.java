package com.spring.interview.transactions.service;

import com.spring.interview.transactions.entity.BankAccountEntity;
import com.spring.interview.transactions.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class AccountTransferService {

    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    private final BankAccountRepository accountRepository;
    private final TransactionTemplate transactionTemplate;

    public AccountTransferService(BankAccountRepository accountRepository, TransactionTemplate transactionTemplate) {
        this.accountRepository = accountRepository;
        this.transactionTemplate = transactionTemplate;
    }

    // Explicit rollbackFor = Exception.class to rollback on checked exceptions!
    @Transactional(rollbackFor = Exception.class)
    public void transferDeclarative(String fromAccNum, String toAccNum, double amount) throws InsufficientFundsException {
        BankAccountEntity from = accountRepository.findByAccountNumber(fromAccNum)
            .orElseThrow(() -> new IllegalArgumentException("From account not found: " + fromAccNum));
        BankAccountEntity to = accountRepository.findByAccountNumber(toAccNum)
            .orElseThrow(() -> new IllegalArgumentException("To account not found: " + toAccNum));

        if (from.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient balance on account " + fromAccNum);
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    // Programmatic transaction boundary via TransactionTemplate
    public boolean transferProgrammatic(String fromAccNum, String toAccNum, double amount) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try {
                transferDeclarative(fromAccNum, toAccNum, amount);
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        }));
    }
}
