package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        log.info("Saving new invoice for customer id={}", invoice.getCustomer() != null ? invoice.getCustomer().getId() : null);
        if (invoice.getCustomer() == null || invoice.getCustomer().getId() == null) {
            log.error("Cannot create invoice: missing customer ID");
            throw new IllegalArgumentException("Customer ID must be provided.");
        }

        Customer customer = customerRepository.findById(invoice.getCustomer().getId())
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", invoice.getCustomer().getId());
                    return new RuntimeException("Customer not found with ID: " + invoice.getCustomer().getId());
                });

        invoice.setCustomer(customer);
        Invoice saved = invoiceRepository.save(invoice);
        log.debug("Invoice saved with id={}", saved.getId());
        return saved;
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        log.info("Retrieving invoice by id={}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Invoice not found: {}", id);
                    return new RuntimeException("Invoice not found: " + id);
                });
        log.debug("Retrieved invoice for customer id={}", invoice.getCustomer().getId());
        return invoice;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        log.info("Retrieving all invoices");
        List<Invoice> list = invoiceRepository.findAll();
        log.debug("Retrieved {} invoices", list.size());
        return list;
    }

    @Override
    public List<Invoice> getInvoicesByCustomerId(Long customerId) {
        log.info("Retrieving invoices for customer id={}", customerId);
        List<Invoice> list = invoiceRepository.findByCustomerId(customerId);
        log.debug("Retrieved {} invoices for customer id={}", list.size(), customerId);
        return list;
    }

    @Override
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        log.info("Updating invoice id={}", id);
        Invoice existing = getInvoiceById(id);

        if (updatedInvoice.getCustomer() != null && updatedInvoice.getCustomer().getId() != null) {
            Customer customer = customerRepository.findById(updatedInvoice.getCustomer().getId())
                    .orElseThrow(() -> {
                        log.error("Customer not found with ID: {}", updatedInvoice.getCustomer().getId());
                        return new RuntimeException("Customer not found with ID: " + updatedInvoice.getCustomer().getId());
                    });
            existing.setCustomer(customer);
            log.debug("Changed customer for invoice id={} to customer id={}", id, customer.getId());
        }

        existing.setTotalAmount(updatedInvoice.getTotalAmount());
        existing.setIssuedAt(updatedInvoice.getIssuedAt());
        Invoice saved = invoiceRepository.save(existing);
        log.debug("Updated invoice id={}", saved.getId());
        return saved;
    }

    @Override
    public void deleteInvoice(Long id) {
        log.info("Deleting invoice id={}", id);
        invoiceRepository.deleteById(id);
    }
}
