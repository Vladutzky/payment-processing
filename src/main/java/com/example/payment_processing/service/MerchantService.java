package com.example.payment_processing.service;

import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.dto.MerchantRevenueDTO;

import java.util.List;

public interface MerchantService {

    Merchant createMerchant(Merchant merchant);

    Merchant getMerchantById(Long id);

    List<Merchant> getAllMerchants();

    Merchant updateMerchant(Long id, Merchant merchant);

    void deleteMerchant(Long id);

    MerchantRevenueDTO calculateTotalRevenue(Long merchantId);
}
