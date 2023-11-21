package com.gfl.walletservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class Account {

    @Id
    private Long id;

    @Column
    private BigDecimal balance;

    @Version
    private Timestamp version;

}
