package com.test.service;

import com.test.dto.AccountDTO;
import com.test.dto.CustomerDTO;
import com.test.entity.Account;
import com.test.entity.Customer;
import com.test.exception.CustomizeException;
import com.test.repository.AccountRepository;
import com.test.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(accountDTO.getCustomer().getId());
        if (!optionalCustomer.isPresent()) {
            throw new CustomizeException("Customer not found");
        }

        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());
        account.setCustomer(optionalCustomer.get());

        accountRepository.save(account);

        accountDTO.setId(account.getId());
        return accountDTO;
    }

    public AccountDTO getAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (!optionalAccount.isPresent()) {
            throw new CustomizeException("Account not found");
        }
        return convertToDTO(optionalAccount.get());
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setCustomer(new CustomerDTO(account.getCustomer()));
        return accountDTO;
    }
}