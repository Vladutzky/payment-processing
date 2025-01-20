package com.example.payment_processing.dto;

import java.util.List;

public class MerchantRevenueDTO {

    private Long merchantId;
    private String merchantName;
    private Double totalRevenue;
    private List<Long> transactionIds;

    public MerchantRevenueDTO() {
    }

    public MerchantRevenueDTO(Long merchantId, String merchantName, Double totalRevenue, List<Long> transactionIds) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.totalRevenue = totalRevenue;
        this.transactionIds = transactionIds;
    }

    // Getters and setters

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Long> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(List<Long> transactionIds) {
        this.transactionIds = transactionIds;
    }
}
