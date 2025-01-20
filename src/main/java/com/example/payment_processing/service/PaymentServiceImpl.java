package com.example.payment_processing.service;

import com.example.payment_processing.model.*;
import com.example.payment_processing.repository.InvoiceRepository;
import com.example.payment_processing.repository.PaymentRepository;
import com.example.payment_processing.repository.TransactionRepository;
import com.example.payment_processing.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Payment createPayment(Payment payment) {



        Customer customer = customerRepository.findById(payment.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        // Create Invoice
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(payment.getAmount());
        invoice.setIssuedAt(payment.getPaymentDate());
        invoice.setCustomer(customer); // Optional, update as needed
        invoiceRepository.save(invoice);

        // Create Transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(payment.getAmount());
        transaction.setTransactionDate(payment.getPaymentDate());

        transactionRepository.save(transaction);

        // Link to Payment
        payment.setInvoice(invoice);
        payment.setTransaction(transaction);


        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment existing = getPaymentById(id);
        existing.setPayerName(updatedPayment.getPayerName());
        existing.setAmount(updatedPayment.getAmount());
        existing.setPaymentDate(updatedPayment.getPaymentDate());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public List<Payment> getPaymentsByMerchant(Long merchantId) {
        return paymentRepository.findByMerchantId(merchantId);
    }

    @Override
    public Double calculateTotalAmountForMerchant(Long merchantId) {
        return getPaymentsByMerchant(merchantId)
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
