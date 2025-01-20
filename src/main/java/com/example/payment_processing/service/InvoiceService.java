package com.example.payment_processing.service;

import com.example.payment_processing.model.Invoice;

import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(Invoice invoice);

    Invoice getInvoiceById(Long id);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByCustomerId(Long customerId);

    Invoice updateInvoice(Long id, Invoice invoice);

    void deleteInvoice(Long id);
}
