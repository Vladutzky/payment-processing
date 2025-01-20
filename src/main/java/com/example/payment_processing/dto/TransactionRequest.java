package com.example.payment_processing.dto;

import jakarta.validation.constraints.Positive;

public class TransactionRequest {

    @Positive
    private Double transactionAmount;

    // getters, setters
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
