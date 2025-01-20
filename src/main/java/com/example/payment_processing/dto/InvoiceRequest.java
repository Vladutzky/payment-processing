package com.example.payment_processing.dto;

import jakarta.validation.constraints.Positive;

public class InvoiceRequest {

    @Positive
    private Double totalAmount;

    // getters, setters
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
