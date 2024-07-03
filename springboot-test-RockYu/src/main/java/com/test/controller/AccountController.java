package com.test.controller;

import com.test.dto.AccountDTO;
import com.test.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccount(id);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
//        List<AccountDTO> accounts = accountService.getAllAccounts();
//        return new ResponseEntity<>(accounts, HttpStatus.OK);
//    }
}