package com.gfl.walletservice.service;

import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.dto.TransferRequest;
import com.gfl.walletservice.dto.TransferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.COMPLETABLE_FUTURE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService service;

    @Test
    @Sql("/createAccounts.sql")
    void account_details_not_found() {
        AccountDto result = service.getAccountDetails(1L);

        assertThat(result).isNotNull();
        assertThat(result.getBalance()).usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(1000));
    }

    @Test
    @Sql("/createAccounts.sql")
    void transfer() throws Exception {
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal(100))
                .build();

        service.transfer(request);

        assertThat(service.getAccountDetails(1L).getBalance())
                .usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(900));
        assertThat(service.getAccountDetails(2L).getBalance())
                .usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(2100));
    }

    @Test
    @Sql("/createAccounts.sql")
    void transferConcurrently() throws Exception {
        TransferRequest request = TransferRequest.builder()
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal(1))
                .build();

        var i = 1000;
        var executor = Executors.newFixedThreadPool(100);
        var results = executor.invokeAll(IntStream.range(0, i).boxed()
                .<Callable<TransferResponse>>map(j -> () -> service.transfer(request))
                .collect(Collectors.toList())
        );

        executor.shutdown();
        var terminated = executor.awaitTermination(30, TimeUnit.SECONDS);

        assertThat(terminated).isTrue();
        assertThat(results.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).count()).isEqualTo(i);
        assertThat(service.getAccountDetails(1L).getBalance())
                .usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(1000 - i));
        assertThat(service.getAccountDetails(2L).getBalance())
                .usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(2000 + i));
    }
}