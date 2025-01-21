package com.example.payment_processing.service;

import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.dto.MerchantRevenueDTO;

import java.util.List;
import java.util.Map;

public interface MerchantService {

    Merchant createMerchant(Merchant merchant);

    Merchant getMerchantById(Long id);

    List<Merchant> getAllMerchants();

    Merchant updateMerchant(Long id, Merchant merchant);
    List<Map<String, Object>> getTopMerchants();

    void deleteMerchant(Long id);

    MerchantRevenueDTO calculateTotalRevenue(Long merchantId);
}
