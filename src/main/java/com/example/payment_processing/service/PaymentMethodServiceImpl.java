package com.example.payment_processing.service;

import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod createPaymentMethod(PaymentMethod method) {
        return paymentMethodRepository.save(method);
    }

    @Override
    public PaymentMethod getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Method not found: " + id));
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedMethod) {
        PaymentMethod existing = getPaymentMethodById(id);
        existing.setMethodName(updatedMethod.getMethodName());
        return paymentMethodRepository.save(existing);
    }

    @Override
    public void deletePaymentMethod(Long id) {
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public long countPaymentsByMethod(Long methodId) {
        PaymentMethod method = getPaymentMethodById(methodId);
        return method.getPayments().size();
    }
}
