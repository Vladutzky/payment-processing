package com.example.payment_processing.service;

import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.repository.PaymentMethodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private static final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod createPaymentMethod(PaymentMethod method) {
        log.info("Creating payment method name={}", method.getMethodName());
        PaymentMethod saved = paymentMethodRepository.save(method);
        log.debug("Saved payment method id={}", saved.getId());
        return saved;
    }

    @Override
    public PaymentMethod getPaymentMethodById(Long id) {
        log.info("Retrieving payment method by id={}", id);
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment method not found: {}", id);
                    return new RuntimeException("Payment Method not found: " + id);
                });
        log.debug("Found payment method name={}", method.getMethodName());
        return method;
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        log.info("Retrieving all payment methods");
        List<PaymentMethod> list = paymentMethodRepository.findAll();
        log.debug("Retrieved {} payment methods", list.size());
        return list;
    }

    @Override
    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedMethod) {
        log.info("Updating payment method id={}", id);
        PaymentMethod existing = getPaymentMethodById(id);
        existing.setMethodName(updatedMethod.getMethodName());
        PaymentMethod saved = paymentMethodRepository.save(existing);
        log.debug("Updated payment method id={}", saved.getId());
        return saved;
    }

    @Override
    public void deletePaymentMethod(Long id) {
        log.info("Deleting payment method id={}", id);
        paymentMethodRepository.deleteById(id);
        log.debug("Deleted payment method id={}", id);
    }

    @Override
    public long countPaymentsByMethod(Long methodId) {
        log.info("Counting payments for method id={}", methodId);
        PaymentMethod method = getPaymentMethodById(methodId);
        long count = method.getPayments().size();
        log.debug("Found {} payments for method id={}", count, methodId);
        return count;
    }
}