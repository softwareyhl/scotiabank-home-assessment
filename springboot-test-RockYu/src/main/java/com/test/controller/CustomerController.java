package com.test.controller;


import com.test.dto.CustomerDTO;
import com.test.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
//        CustomerDTO customerDTO = customerService.getCustomer(id);
//        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
//        List<CustomerDTO> customers = customerService.getAllCustomers();
//        return new ResponseEntity<>(customers, HttpStatus.OK);
//    }
}