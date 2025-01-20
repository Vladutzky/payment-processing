package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Customer ID must be provided.");
        }

        // Fetch the Customer entity by ID
        Customer customer = customerRepository.findById(invoice.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + invoice.getCustomer().getId()));

        // Set the customer in the invoice
        invoice.setCustomer(customer);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> getInvoicesByCustomerId(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }

    @Override
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        Invoice existing = getInvoiceById(id);

        if (updatedInvoice.getCustomer() != null && updatedInvoice.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(updatedInvoice.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + updatedInvoice.getCustomer().getId()));
            existing.setCustomer(customer);
        }

        existing.setTotalAmount(updatedInvoice.getTotalAmount());
        existing.setIssuedAt(updatedInvoice.getIssuedAt());
        return invoiceRepository.save(existing);
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
