package com.gfl.walletservice.rest;

import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.dto.TransferRequest;
import com.gfl.walletservice.dto.TransferResponse;
import com.gfl.walletservice.rest.api.AccountApi;
import com.gfl.walletservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@RestController
@RequestMapping(value = "/account", produces = APPLICATION_JSON)
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;


    @Override
    @GetMapping("/{id}")
    public AccountDto getAccountDetails(@PathVariable("id") Long id) {
        return accountService.getAccountDetails(id);
    }

    @Override
    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        return accountService.transfer(request);
    }
}
