package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomersSorted(String sortField, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        return customerRepository.findAll(sort);
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

    @Override
    public Double getTotalSpendingByCustomerId(Long customerId) {
        Customer c = getCustomerById(customerId);
        return c.getInvoices().stream()
                .flatMap(inv -> inv.getPayments().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public Customer getTopSpendingCustomer() {
        List<Customer> all = customerRepository.findAll();
        return all.stream()
                .max(Comparator.comparingDouble(this::calculateTotalSpent))
                .orElseThrow(() -> new RuntimeException("No customers found"));
    }

    private double calculateTotalSpent(Customer customer) {
        return customer.getInvoices().stream()
                .flatMap(inv -> inv.getPayments().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
