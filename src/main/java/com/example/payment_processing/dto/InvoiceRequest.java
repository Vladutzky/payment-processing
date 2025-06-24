package com.example.payment_processing.dto;

import jakarta.validation.constraints.NotNull;

public class InvoiceRequest {
    private Long id;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    public InvoiceRequest() {}
    public InvoiceRequest(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
