package com.gfl.walletservice.rest;

import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.dto.TransferRequest;
import com.gfl.walletservice.dto.TransferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;

@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {


    @Autowired
    private WebTestClient webTestClient;


    @Test
    @Sql("/createAccounts.sql")
    void getAccountDetails() {
        webTestClient.get()
                .uri("/account/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(AccountDto.class).isEqualTo(AccountDto.builder()
                        .id(1L)
                        .balance(new BigDecimal("1000.00"))
                        .build());

    }

    @Test
    @Sql("/createAccounts.sql")
    void transfer() {
        webTestClient.post()
                .uri("/account/transfer")
                .body(BodyInserters.fromValue(TransferRequest.builder()
                        .fromAccountId(2L)
                        .toAccountId(3L)
                        .amount(new BigDecimal("100"))
                        .build()))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(TransferResponse.class).isEqualTo(TransferResponse.builder()
                        .amount(new BigDecimal("100"))
                        .build());
    }
}