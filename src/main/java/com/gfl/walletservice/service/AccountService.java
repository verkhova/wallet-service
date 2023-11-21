package com.gfl.walletservice.service;

import com.gfl.walletservice.exception.NotEnoughBalanceException;
import com.gfl.walletservice.exception.ResourceNotFoundException;
import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.dto.TransferRequest;
import com.gfl.walletservice.dto.TransferResponse;
import com.gfl.walletservice.entity.Account;
import com.gfl.walletservice.mapper.AccountMapper;
import com.gfl.walletservice.repository.AccountRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final EntityManager entityManager;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto getAccountDetails(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account %s not found", id)));
    }

    @Retryable(retryFor = { PersistenceException.class },
            maxAttempts = 10,
            backoff = @Backoff(delay = 10, multiplier = 2, random = true))
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        var fromAccount = entityManager.find(Account.class, request.getFromAccountId(), LockModeType.PESSIMISTIC_WRITE);
        var toAccount = entityManager.find(Account.class, request.getToAccountId(), LockModeType.PESSIMISTIC_WRITE);

        BigDecimal fromAccountRemaining = fromAccount.getBalance().subtract(request.getAmount());
        if (fromAccountRemaining.compareTo(BigDecimal.ZERO) >= 0) {
            fromAccount.setBalance(fromAccountRemaining);
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        } else {
            throw new NotEnoughBalanceException();
        }
        log.info("Transferred balance {}, from {}, to {}", request, fromAccount, toAccount);
        return TransferResponse.builder()
                .amount(request.getAmount())
                .build();
    }

}
