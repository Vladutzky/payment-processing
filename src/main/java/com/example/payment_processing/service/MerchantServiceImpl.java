package com.example.payment_processing.service;

import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.dto.MerchantRevenueDTO;
import com.example.payment_processing.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant createMerchant(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Merchant not found: " + id));
    }

    @Override
    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }
    @Override
    public List<Map<String, Object>> getTopMerchants() {
        List<Object[]> results = merchantRepository.findTopMerchants();
        return results.stream().map(r -> {
            Map<String, Object> merchantData = new HashMap<>();
            merchantData.put("merchantId", r[0]);
            merchantData.put("merchantName", r[1]);
            merchantData.put("totalRevenue", r[2]);
            return merchantData;
        }).collect(Collectors.toList());
    }


    @Override
    public Merchant updateMerchant(Long id, Merchant updatedMerchant) {
        Merchant existing = getMerchantById(id);
        existing.setMerchantName(updatedMerchant.getMerchantName());
        existing.setMerchantEmail(updatedMerchant.getMerchantEmail());
        return merchantRepository.save(existing);
    }

    @Override
    public void deleteMerchant(Long id) {
        merchantRepository.deleteById(id);
    }

    @Override
    public MerchantRevenueDTO calculateTotalRevenue(Long merchantId) {
        Merchant merchant = getMerchantById(merchantId);

        // Calculate total revenue
        double totalRevenue = merchant.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        // Safely collect transaction IDs, skipping null transactions
        List<Long> transactionIds = merchant.getPayments().stream()
                .map(Payment::getTransaction)
                .filter(transaction -> transaction != null)
                .map(Transaction::getId)
                .toList();

        return new MerchantRevenueDTO(
                merchant.getId(),
                merchant.getMerchantName(),
                totalRevenue,
                transactionIds
        );
    }

}
