package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        log.info("Saving new customer: {}", customer.getCustomerEmail());
        Customer saved = customerRepository.save(customer);
        log.debug("Saved customer id={}", saved.getId());
        return saved;
    }

    @Override
    public Customer getCustomerById(Long id) {
        log.info("Retrieving customer by id={}", id);
        Customer found = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
        log.debug("Found customer: {}", found.getCustomerEmail());
        return found;
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("Retrieving all customers");
        List<Customer> list = customerRepository.findAll();
        log.debug("Retrieved {} customers", list.size());
        return list;
    }

    @Override
    public List<Customer> getAllCustomersSorted(String sortField, String sortDir) {
        log.info("Retrieving all customers sorted by {} {}", sortField, sortDir);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        List<Customer> list = customerRepository.findAll(sort);
        log.debug("Retrieved {} customers", list.size());
        return list;
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        log.info("Updating customer id={}", id);
        Customer existing = getCustomerById(id);
        existing.setCustomerName(updatedCustomer.getCustomerName());
        existing.setCustomerEmail(updatedCustomer.getCustomerEmail());
        Customer saved = customerRepository.save(existing);
        log.debug("Updated customer id={}", saved.getId());
        return saved;
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer id={}", id);
        customerRepository.deleteById(id);
    }

    @Override
    public Double getTotalSpendingByCustomerId(Long customerId) {
        log.info("Calculating total spending for customer id={}", customerId);
        Customer c = getCustomerById(customerId);
        double total = c.getInvoices().stream()
                .flatMap(inv -> inv.getPayments().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
        log.debug("Total spending for id={} is {}", customerId, total);
        return total;
    }

    @Override
    public Customer getTopSpendingCustomer() {
        log.info("Finding top spending customer");
        List<Customer> all = customerRepository.findAll();
        Customer top = all.stream()
                .max(Comparator.comparingDouble(this::calculateTotalSpent))
                .orElseThrow(() -> new RuntimeException("No customers found"));
        log.debug("Top spender is id={} email={}", top.getId(), top.getCustomerEmail());
        return top;
    }

    private double calculateTotalSpent(Customer customer) {
        return customer.getInvoices().stream()
                .flatMap(inv -> inv.getPayments().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
