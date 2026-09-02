package com.spring.interview.transactions.service;

import com.spring.interview.transactions.entity.BankAccountEntity;
import com.spring.interview.transactions.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AccountTransferServiceTest {

    @Autowired
    private AccountTransferService transferService;

    @Autowired
    private BankAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        accountRepository.save(new BankAccountEntity("ACC-100", 1000.0));
        accountRepository.save(new BankAccountEntity("ACC-200", 500.0));
    }

    @Test
    @DisplayName("Should successfully transfer funds and commit changes atomically")
    void shouldTransferFundsSuccessfully() throws Exception {
        transferService.transferDeclarative("ACC-100", "ACC-200", 200.0);

        BankAccountEntity acc1 = accountRepository.findByAccountNumber("ACC-100").orElseThrow();
        BankAccountEntity acc2 = accountRepository.findByAccountNumber("ACC-200").orElseThrow();

        assertThat(acc1.getBalance()).isEqualTo(800.0);
        assertThat(acc2.getBalance()).isEqualTo(700.0);
    }

    @Test
    @DisplayName("Should rollback on checked InsufficientFundsException when configured with rollbackFor")
    void shouldRollbackOnInsufficientFunds() {
        assertThatThrownBy(() -> transferService.transferDeclarative("ACC-100", "ACC-200", 2000.0))
            .isInstanceOf(AccountTransferService.InsufficientFundsException.class);

        BankAccountEntity acc1 = accountRepository.findByAccountNumber("ACC-100").orElseThrow();
        BankAccountEntity acc2 = accountRepository.findByAccountNumber("ACC-200").orElseThrow();

        // Balances must be untouched due to rollback!
        assertThat(acc1.getBalance()).isEqualTo(1000.0);
        assertThat(acc2.getBalance()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Should handle programmatic transfers via TransactionTemplate")
    void shouldTransferProgrammatically() {
        boolean success = transferService.transferProgrammatic("ACC-100", "ACC-200", 300.0);
        assertThat(success).isTrue();

        BankAccountEntity acc1 = accountRepository.findByAccountNumber("ACC-100").orElseThrow();
        assertThat(acc1.getBalance()).isEqualTo(700.0);
    }
}
