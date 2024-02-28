package com.elf.fundtransfer.domain.model;

import com.elf.fundtransfer.domain.model.valueobject.Currency;
import com.elf.fundtransfer.infrastructure.util.UUIDConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long transferId;
    private Long debitAccountId;
    private Long creditAccountId;
    private double amount;
    private Date timestamp;
    private Currency currency;

    @Transient
    private UUID transferIdUUID;

    // Method to convert the transferId from UUID to Long
    public void setTransferId(UUID transferIdUUID) {
        this.transferIdUUID = transferIdUUID;
        this.transferId = UUIDConverter.convertUUIDToLong(transferIdUUID);
    }

    // Method to convert the transferId from Long to UUID
    public UUID getTransferId() {
        if (transferIdUUID == null && transferId != null) {
            transferIdUUID = UUIDConverter.convertLongToUUID(transferId);
        }
        return transferIdUUID;
    }
}
