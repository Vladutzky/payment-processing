package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.InvoiceRepository;
import com.example.payment_processing.repository.MerchantRepository;
import com.example.payment_processing.repository.PaymentMethodRepository;
import com.example.payment_processing.repository.PaymentRepository;
import com.example.payment_processing.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

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
        log.info("Creating payment record for invoiceId={} customerId={} merchantId={} amount={}",
                payment.getInvoice().getId(), payment.getCustomer().getId(),
                payment.getMerchant().getId(), payment.getAmount());
        Customer customer = customerRepository.findById(payment.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + payment.getCustomer().getId()));
        Invoice invoice = invoiceRepository.findById(payment.getInvoice().getId())
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + payment.getInvoice().getId()));
        Merchant merchant = payment.getMerchant();
        PaymentMethod pm = payment.getPaymentMethod();

        payment.setCustomer(customer);
        payment.setInvoice(invoice);
        payment.setMerchant(merchant);
        payment.setPaymentMethod(pm);
        Payment saved = paymentRepository.save(payment);
        log.debug("Saved payment id={}", saved.getId());

        Transaction txn = new Transaction();
        txn.setTransactionAmount(saved.getAmount());
        txn.setTransactionDate(saved.getPaymentDate());
        txn.setCustomer(customer);
        txn.setInvoice(invoice);
        txn.setMerchant(merchant);
        txn.setPayment(saved);
        Transaction createdTxn = transactionRepository.save(txn);
        log.debug("Created transaction id={} for payment id={}", createdTxn.getId(), saved.getId());

        saved.setTransaction(createdTxn);
        Payment finalSaved = paymentRepository.save(saved);
        log.debug("Linked transaction to payment id={}", finalSaved.getId());
        return finalSaved;
    }

    @Override
    public void refundPayment(Long paymentId) {
        log.info("Processing refund for payment id={}", paymentId);
        Payment original = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        if (original.isRefunded()) {
            log.warn("Payment id={} already refunded", paymentId);
            throw new RuntimeException("Already refunded: " + paymentId);
        }
        original.setRefunded(true);
        paymentRepository.save(original);
        log.debug("Marked payment id={} as refunded", paymentId);

        Payment refund = new Payment();
        refund.setPayerName(original.getPayerName());
        refund.setAmount(-original.getAmount());
        refund.setPaymentDate(LocalDateTime.now());
        refund.setMerchant(original.getMerchant());
        refund.setPaymentMethod(original.getPaymentMethod());
        refund.setInvoice(original.getInvoice());
        refund.setCustomer(original.getCustomer());
        refund.setRefunded(false);
        Payment savedRefund = paymentRepository.save(refund);
        log.debug("Created refund payment id={} for original id={}", savedRefund.getId(), paymentId);
    }

    @Override
    public Map<String, Double> getPaymentMethodUsagePercentage(Long customerId) {
        log.info("Calculating payment method usage percentage for customer id={}", customerId);
        List<Object[]> counts = paymentRepository.countPaymentsByMethodForCustomer(customerId);
        long total = counts.stream().mapToLong(r -> (Long) r[1]).sum();
        Map<String, Double> result = counts.stream().collect(Collectors.toMap(
                r -> paymentMethodRepository.findById((Long) r[0])
                        .map(PaymentMethod::getMethodName)
                        .orElse("Unknown"),
                r -> ((Long) r[1] * 100.0) / total
        ));
        log.debug("Computed payment method usage: {}", result);
        return result;
    }

    @Override
    public Payment getPaymentById(Long id) {
        log.info("Retrieving payment by id={}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found: {}", id);
                    return new RuntimeException("Payment not found: " + id);
                });
        log.debug("Retrieved payment id={} amount={}", id, payment.getAmount());
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        log.info("Retrieving all payments");
        List<Payment> list = paymentRepository.findAll();
        log.debug("Retrieved {} payments", list.size());
        return list;
    }

    @Override
    public List<Payment> getAllPaymentsSorted(String sortField, String sortDir) {
        log.info("Retrieving all payments sorted by {} {}", sortField, sortDir);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        List<Payment> list = paymentRepository.findAll(sort);
        log.debug("Retrieved {} payments sorted", list.size());
        return list;
    }

    @Override
    public Payment updatePayment(Long id, Payment updatedPayment) {
        log.info("Updating payment id={}", id);
        Payment existing = getPaymentById(id);
        existing.setPayerName(updatedPayment.getPayerName());
        existing.setAmount(updatedPayment.getAmount());
        existing.setPaymentDate(updatedPayment.getPaymentDate());
        existing.setMerchant(updatedPayment.getMerchant());
        existing.setPaymentMethod(updatedPayment.getPaymentMethod());
        existing.setInvoice(updatedPayment.getInvoice());
        existing.setCustomer(updatedPayment.getCustomer());
        Payment saved = paymentRepository.save(existing);
        log.debug("Updated payment id={}", saved.getId());
        return saved;
    }

    @Override
    public void deletePayment(Long id) {
        log.info("Deleting payment id={}", id);
        paymentRepository.deleteById(id);
        log.debug("Deleted payment id={}", id);
    }

    @Override
    public List<Payment> getPaymentsByMerchant(Long merchantId) {
        log.info("Retrieving payments for merchant id={}", merchantId);
        List<Payment> list = paymentRepository.findByMerchantId(merchantId);
        log.debug("Retrieved {} payments for merchant id={}", list.size(), merchantId);
        return list;
    }

    @Override
    public Double calculateTotalAmountForMerchant(Long merchantId) {
        log.info("Calculating total amount for merchant id={}", merchantId);
        double total = getPaymentsByMerchant(merchantId).stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        log.debug("Total amount for merchant id={} is {}", merchantId, total);
        return total;
    }
}
