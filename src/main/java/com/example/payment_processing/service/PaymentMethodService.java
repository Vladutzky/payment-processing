package com.example.payment_processing.service;

import com.example.payment_processing.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    PaymentMethod createPaymentMethod(PaymentMethod method);

    PaymentMethod getPaymentMethodById(Long id);

    List<PaymentMethod> getAllPaymentMethods();

    PaymentMethod updatePaymentMethod(Long id, PaymentMethod method);

    void deletePaymentMethod(Long id);

    long countPaymentsByMethod(Long methodId);
}
