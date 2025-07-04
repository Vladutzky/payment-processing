package com.example.payment_processing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "merchants")
@Schema(name = "Merchant", description = "Represents a merchant entity in the system")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantName;
    private String merchantEmail;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @ManyToMany(mappedBy = "merchants")
    private Set<Customer> customers = new HashSet<>();


    public Merchant() {}

    public Merchant(String merchantName, String merchantEmail) {
        this.merchantName = merchantName;
        this.merchantEmail = merchantEmail;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
