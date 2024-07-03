package com.test.service;

import com.test.dto.CustomerDTO;
import com.test.entity.Customer;
import com.test.exception.CustomizeException;
import com.test.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(bCryptPasswordEncoder.encode(customerDTO.getPassword()));

        customerRepository.save(customer);

        customerDTO.setId(customer.getId());
        log.info("Customer created successfully: {}", customerDTO);
        return customerDTO;
    }

    public CustomerDTO getCustomer(Long id) {
        log.info("Getting customer with id: {}", id);
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            log.error("Customer not found with id: {}", id);
            throw new CustomizeException("Customer not found");
        }

        log.info("Customer found: {}", optionalCustomer.get());
        return convertToDTO(optionalCustomer.get());
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
}