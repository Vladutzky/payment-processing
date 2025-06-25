package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    // Existing methods
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
    List<Customer> getAllCustomersSorted(String sortField, String sortDir);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
    Double getTotalSpendingByCustomerId(Long customerId);
    Customer getTopSpendingCustomer();


}
