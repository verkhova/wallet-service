package com.gfl.walletservice.dto;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@Builder
@Value
public class AccountDto {

    private final Long id;
    private final BigDecimal balance;

}
