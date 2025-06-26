package com.example.payment_processing.service;

import com.example.payment_processing.dto.MerchantRevenueDTO;
import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.repository.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {
    private static final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Merchant createMerchant(Merchant merchant) {
        log.info("Creating merchant name={} email={}", merchant.getMerchantName(), merchant.getMerchantEmail());
        Merchant saved = merchantRepository.save(merchant);
        log.debug("Saved merchant id={}", saved.getId());
        return saved;
    }

    @Override
    public Merchant getMerchantById(Long id) {
        log.info("Retrieving merchant by id={}", id);
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Merchant not found: {}", id);
                    return new RuntimeException("Merchant not found: " + id);
                });
        log.debug("Found merchant name={} email={}", merchant.getMerchantName(), merchant.getMerchantEmail());
        return merchant;
    }

    @Override
    public List<Merchant> getAllMerchants() {
        log.info("Retrieving all merchants");
        List<Merchant> list = merchantRepository.findAll();
        log.debug("Retrieved {} merchants", list.size());
        return list;
    }

    @Override
    public List<Map<String, Object>> getTopMerchants() {
        log.info("Calculating top merchants");
        List<Object[]> results = merchantRepository.findTopMerchants();
        List<Map<String, Object>> topList = results.stream().map(r -> {
            Map<String, Object> data = new HashMap<>();
            data.put("merchantId", r[0]);
            data.put("merchantName", r[1]);
            data.put("totalRevenue", r[2]);
            return data;
        }).collect(Collectors.toList());
        log.debug("Top merchants count={} ", topList.size());
        return topList;
    }

    @Override
    public Merchant updateMerchant(Long id, Merchant updatedMerchant) {
        log.info("Updating merchant id={}", id);
        Merchant existing = getMerchantById(id);
        existing.setMerchantName(updatedMerchant.getMerchantName());
        existing.setMerchantEmail(updatedMerchant.getMerchantEmail());
        Merchant saved = merchantRepository.save(existing);
        log.debug("Updated merchant id={}", saved.getId());
        return saved;
    }

    @Override
    public void deleteMerchant(Long id) {
        log.info("Deleting merchant id={}", id);
        merchantRepository.deleteById(id);
        log.debug("Deleted merchant id={}", id);
    }

    @Override
    public MerchantRevenueDTO calculateTotalRevenue(Long merchantId) {
        log.info("Calculating total revenue for merchant id={}", merchantId);
        Merchant merchant = getMerchantById(merchantId);

        double totalRevenue = merchant.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        log.debug("Total revenue computed={} for merchant id={}", totalRevenue, merchantId);

        List<Long> transactionIds = merchant.getPayments().stream()
                .map(Payment::getTransaction)
                .filter(tx -> tx != null)
                .map(Transaction::getId)
                .toList();
        log.debug("Found {} transactions for merchant id={}", transactionIds.size(), merchantId);

        MerchantRevenueDTO dto = new MerchantRevenueDTO(
                merchant.getId(),
                merchant.getMerchantName(),
                totalRevenue,
                transactionIds
        );
        log.info("Returning revenue DTO for merchant id={}", merchantId);
        return dto;
    }
}