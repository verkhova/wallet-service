package com.gfl.walletservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@Value
@Builder
public class TransferResponse {
    private final BigDecimal amount;
}
