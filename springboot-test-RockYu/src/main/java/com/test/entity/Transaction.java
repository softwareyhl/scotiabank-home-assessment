package com.test.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Transaction extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}