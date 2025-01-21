package com.example.payment_processing.service;

import com.example.payment_processing.model.*;
import com.example.payment_processing.repository.InvoiceRepository;
import com.example.payment_processing.repository.PaymentRepository;
import com.example.payment_processing.repository.TransactionRepository;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

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
    public void refundPayment(Long paymentId) {
        // Find the original payment
        Payment originalPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        // Check if the payment is already refunded
        if (originalPayment.isRefunded()) {
            throw new RuntimeException("Payment has already been refunded.");
        }

        // Mark the original payment as refunded
        originalPayment.setRefunded(true);
        paymentRepository.save(originalPayment);

        // Create a new payment with negative amount for refund
        Payment refundPayment = new Payment();
        refundPayment.setPayerName(originalPayment.getPayerName());
        refundPayment.setAmount(-originalPayment.getAmount());
        refundPayment.setPaymentDate(LocalDateTime.now());
        refundPayment.setMerchant(originalPayment.getMerchant());
        refundPayment.setPaymentMethod(originalPayment.getPaymentMethod());
        refundPayment.setInvoice(originalPayment.getInvoice());
        refundPayment.setRefunded(false); // Refund payments are not refundable

        // Save the refund payment
        paymentRepository.save(refundPayment);
    }
    public Map<String, Double> getPaymentMethodUsagePercentage(Long customerId) {
        List<Object[]> results = paymentRepository.countPaymentsByMethodForCustomer(customerId);
        long totalPayments = results.stream().mapToLong(r -> (long) r[1]).sum();

        return results.stream().collect(Collectors.toMap(
                r -> paymentMethodRepository.findById((Long) r[0]).get().getMethodName(),
                r -> ((long) r[1] * 100.0) / totalPayments
        ));
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
