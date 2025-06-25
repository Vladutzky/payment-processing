package com.example.payment_processing.service;

import com.example.payment_processing.model.*;
import com.example.payment_processing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        // Lookup entities
        Customer customer = customerRepository.findById(payment.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + payment.getCustomer().getId()));
        Invoice invoice = invoiceRepository.findById(payment.getInvoice().getId())
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + payment.getInvoice().getId()));
        Merchant merchant = payment.getMerchant();
        PaymentMethod pm = payment.getPaymentMethod();

        // Persist the payment first to generate ID if needed
        payment.setCustomer(customer);
        payment.setInvoice(invoice);
        payment.setMerchant(merchant);
        payment.setPaymentMethod(pm);
        Payment saved = paymentRepository.save(payment);

        // Create transaction record
        Transaction txn = new Transaction();
        txn.setTransactionAmount(saved.getAmount());
        txn.setTransactionDate(saved.getPaymentDate());
        txn.setCustomer(customer);
        txn.setInvoice(invoice);
        txn.setMerchant(merchant);
        txn.setPayment(saved);
        transactionRepository.save(txn);

        // Link back
        saved.setTransaction(txn);
        return paymentRepository.save(saved);
    }

    @Override
    public void refundPayment(Long paymentId) {
        Payment original = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        if (original.isRefunded()) {
            throw new RuntimeException("Already refunded: " + paymentId);
        }
        original.setRefunded(true);
        paymentRepository.save(original);

        Payment refund = new Payment();
        refund.setPayerName(original.getPayerName());
        refund.setAmount(-original.getAmount());
        refund.setPaymentDate(LocalDateTime.now());
        refund.setMerchant(original.getMerchant());
        refund.setPaymentMethod(original.getPaymentMethod());
        refund.setInvoice(original.getInvoice());
        refund.setCustomer(original.getCustomer());
        refund.setRefunded(false);

        paymentRepository.save(refund);
    }

    @Override
    public Map<String, Double> getPaymentMethodUsagePercentage(Long customerId) {
        List<Object[]> counts = paymentRepository.countPaymentsByMethodForCustomer(customerId);
        long total = counts.stream().mapToLong(r -> (Long) r[1]).sum();
        return counts.stream().collect(Collectors.toMap(
                r -> paymentMethodRepository.findById((Long) r[0])
                        .map(PaymentMethod::getMethodName)
                        .orElse("Unknown"),
                r -> ((Long) r[1] * 100.0) / total
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
    public List<Payment> getAllPaymentsSorted(String sortField, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        return paymentRepository.findAll(sort);
    }

    @Override
    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment existing = getPaymentById(id);
        existing.setPayerName(updatedPayment.getPayerName());
        existing.setAmount(updatedPayment.getAmount());
        existing.setPaymentDate(updatedPayment.getPaymentDate());
        existing.setMerchant(updatedPayment.getMerchant());
        existing.setPaymentMethod(updatedPayment.getPaymentMethod());
        existing.setInvoice(updatedPayment.getInvoice());
        existing.setCustomer(updatedPayment.getCustomer());
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
        return getPaymentsByMerchant(merchantId).stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
