package com.example.payment_processing.service;

import com.example.payment_processing.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment getPaymentById(Long id);

    List<Payment> getAllPayments();

    Payment updatePayment(Long id, Payment payment);

    void deletePayment(Long id);

    List<Payment> getPaymentsByMerchant(Long merchantId);

    Double calculateTotalAmountForMerchant(Long merchantId);
}
