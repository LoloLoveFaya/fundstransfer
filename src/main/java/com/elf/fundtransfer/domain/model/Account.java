package com.elf.fundtransfer.domain.model;

import com.elf.fundtransfer.domain.model.valueobject.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Account {
    private Long ownerId;
    private Currency currency;
    private double balance;
}
