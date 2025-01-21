package com.example.payment_processing.controller;

import com.example.payment_processing.model.Payment;
import com.example.payment_processing.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment created = paymentService.createPayment(payment);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment found = paymentService.getPaymentById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    @GetMapping("/{customerId}/payment-method-usage")
    public ResponseEntity<Map<String, Double>> getPaymentMethodUsagePercentage(@PathVariable Long customerId) {
        Map<String, Double> usagePercentage = paymentService.getPaymentMethodUsagePercentage(customerId);
        return ResponseEntity.ok(usagePercentage);
    }
    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        paymentService.refundPayment(id);
        Payment refundedPayment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(refundedPayment);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment
    ) {
        Payment updated = paymentService.updatePayment(id, payment);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
