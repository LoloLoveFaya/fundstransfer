package com.elf.fundtransfer.domain.model;

import com.elf.fundtransfer.domain.model.valueobject.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private Long transferId;
    private Long debitAccountId;
    private Long creditAccountId;
    private double amount;
    private Date timestamp;
    private Currency currency;
}
