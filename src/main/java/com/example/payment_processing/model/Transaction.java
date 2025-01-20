package com.example.payment_processing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double transactionAmount;

    private LocalDateTime transactionDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "customer_id")

    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "invoice_id")

    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "merchant_id")

    private Merchant merchant;

    @OneToOne
    @JoinColumn(name = "payment_id")

    private Payment payment;

    public Transaction() {}

    public Transaction(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }



    // Getters and setters

    public Long getId() {
        return id;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
