package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.payment_processing.model.Payment;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }
    @Override
    public Double getTotalSpendingByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        return customer.getInvoices().stream()
                .flatMap(invoice -> invoice.getPayments().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerById(id);
        existing.setCustomerName(updatedCustomer.getCustomerName());
        existing.setCustomerEmail(updatedCustomer.getCustomerEmail());
        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // New Methods

    @Override
    public List<Customer> getCustomersWithInvoices() {
        return customerRepository.findAll().stream()
                .filter(customer -> !customer.getInvoices().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> getCustomersWithTransactions() {
        return customerRepository.findAll().stream()
                .filter(customer -> !customer.getTransactions().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findCustomersByCriteria(String name, String email) {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getCustomerName().equalsIgnoreCase(name) ||
                        customer.getCustomerEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }
}
