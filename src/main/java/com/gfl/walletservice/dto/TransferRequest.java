package com.gfl.walletservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@Valid
@Builder
@Value
public class TransferRequest {

    @NotNull
    @Positive
    private final Long toAccountId;
    @NotNull
    @Positive
    private final Long fromAccountId;
    @NotNull
    @Positive
    private final BigDecimal amount;


}
