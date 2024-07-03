package com.test.service;

import com.test.dto.TransactionDTO;
import com.test.entity.Account;
import com.test.entity.Transaction;
import com.test.exception.CustomizeException;
import com.test.repository.AccountRepository;
import com.test.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        log.info("Creating transaction: {}", transactionDTO);
        Optional<Account> optionalAccount = accountRepository.findById(transactionDTO.getAccountId());
        if (!optionalAccount.isPresent()) {
            log.error("Account not found: {}", transactionDTO.getAccountId());
            throw new CustomizeException("Account not found");
        }

        Account account = optionalAccount.get();
        Transaction transaction = new Transaction();
        transaction.setType(transactionDTO.getType());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        switch (transactionDTO.getType()) {
            case DEPOSIT:
                account.setBalance(account.getBalance() + transactionDTO.getAmount());
                break;
            case WITHDRAWAL:
                if (account.getBalance() < transactionDTO.getAmount()) {
                    throw new CustomizeException("Insufficient funds");
                }
                account.setBalance(account.getBalance() - transactionDTO.getAmount());
                break;
            case TRANSFER:
                if (account.getBalance() < transactionDTO.getAmount()) {
                    throw new CustomizeException("Insufficient funds");
                }
                Optional<Account> optionalRecipientAccount = accountRepository.findById(transactionDTO.getReceiverAccountId());
                if (!optionalRecipientAccount.isPresent()) {
                    throw new CustomizeException("Recipient account not found");
                }
                Account recipientAccount = optionalRecipientAccount.get();
                account.setBalance(account.getBalance() - transactionDTO.getAmount());
                recipientAccount.setBalance(recipientAccount.getBalance() + transactionDTO.getAmount());
                accountRepository.save(recipientAccount);
                break;
        }

        transactionRepository.save(transaction);
        accountRepository.save(account);

        transactionDTO.setId(transaction.getId());
        transactionDTO.setTimestamp(transaction.getTimestamp());

        log.info("Transaction created: {}", transactionDTO);
        return transactionDTO;
    }

    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getAccount().getId().equals(accountId))
                .collect(Collectors.toList());

        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTimestamp(transaction.getTimestamp());
        transactionDTO.setAccountId(transaction.getAccount().getId());
        return transactionDTO;
    }
}