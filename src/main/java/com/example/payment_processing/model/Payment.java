package com.example.payment_processing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String payerName;


    private Double amount;
    private boolean refunded = false;

    private LocalDateTime paymentDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER) // Ensure invoice is fully loaded
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER) // Ensure merchant is fully loaded
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.EAGER) // Ensure payment method is fully loaded
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER) // New association with customer
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Transaction transaction;

    public Payment() {
    }

    public Payment(String payerName, Double amount) {
        this.payerName = payerName;
        this.amount = amount;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
