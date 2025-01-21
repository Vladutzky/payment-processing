package com.example.payment_processing.controller;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "CustomerController", description = "Manage customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer created = customerService.createCustomer(customer);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Customer found = customerService.getCustomerById(id);
        return ResponseEntity.ok(found);
    }
    @GetMapping("/top-spender")
    public ResponseEntity<Customer> getTopSpendingCustomer() {
        Customer topCustomer = customerService.getTopSpendingCustomer();
        return ResponseEntity.ok(topCustomer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}/total-spending")
    public ResponseEntity<Double> getTotalSpending(@PathVariable Long id) {
        Double totalSpending = customerService.getTotalSpendingByCustomerId(id);
        return ResponseEntity.ok(totalSpending);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer
    ) {
        Customer updated = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
