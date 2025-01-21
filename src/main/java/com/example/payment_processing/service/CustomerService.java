package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    // Existing methods
    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> getAllCustomers();

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    // New methods for handling relationships
    List<Customer> getCustomersWithInvoices();

    List<Customer> getCustomersWithTransactions();
    Double getTotalSpendingByCustomerId(Long customerId);
    Customer getTopSpendingCustomer();

    List<Customer> findCustomersByCriteria(String name, String email);
}
