package com.gfl.walletservice.rest.api;

import com.gfl.walletservice.dto.AccountDto;
import com.gfl.walletservice.dto.TransferRequest;
import com.gfl.walletservice.dto.TransferResponse;

public interface AccountApi {

    AccountDto getAccountDetails(Long id);

    TransferResponse transfer(TransferRequest request);

}
