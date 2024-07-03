package com.test.dto;

import com.test.entity.Transaction;
import com.test.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    @NotNull(message = "Transaction type is required")
    private TransactionType type;
    private Double amount;
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
    private LocalDateTime timestamp;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @NotNull(message = "Account ID is required")
    private Long accountId;
    private Long receiverAccountId;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.timestamp = transaction.getTimestamp();
        this.accountId = transaction.getAccount().getId();
    }
}