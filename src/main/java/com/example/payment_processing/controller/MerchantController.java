package com.example.payment_processing.controller;

import com.example.payment_processing.dto.MerchantRevenueDTO;
import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.service.MerchantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/merchants")
@Tag(name = "MerchantController", description = "Manage merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity<Merchant> createMerchant(@RequestBody Merchant merchant) {
        Merchant created = merchantService.createMerchant(merchant);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchant> getMerchant(@PathVariable Long id) {
        Merchant found = merchantService.getMerchantById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/{id}/revenue")
    public ResponseEntity<MerchantRevenueDTO> calculateTotalRevenue(@PathVariable Long id) {
        MerchantRevenueDTO revenueDTO = merchantService.calculateTotalRevenue(id);
        return ResponseEntity.ok(revenueDTO);
    }
    @GetMapping
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        List<Merchant> merchants = merchantService.getAllMerchants();
        return ResponseEntity.ok(merchants);
    }

    @GetMapping("/top-merchants")
    public ResponseEntity<List<Map<String, Object>>> getTopMerchants() {
        return ResponseEntity.ok(merchantService.getTopMerchants());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Merchant> updateMerchant(
            @PathVariable Long id,
            @RequestBody Merchant merchant
    ) {
        Merchant updated = merchantService.updateMerchant(id, merchant);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }
}
