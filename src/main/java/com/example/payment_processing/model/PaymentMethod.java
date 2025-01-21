package com.example.payment_processing.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName; // e.g., "CREDIT_CARD", "PAYPAL"

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    public PaymentMethod() {}

    public PaymentMethod(String methodName) {
        this.methodName = methodName;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
