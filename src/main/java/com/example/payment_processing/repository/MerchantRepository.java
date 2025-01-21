package com.example.payment_processing.repository;

import com.example.payment_processing.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Query("SELECT m.id, m.merchantName, SUM(p.amount) AS totalRevenue FROM Payment p JOIN p.merchant m GROUP BY m.id ORDER BY totalRevenue DESC")
    List<Object[]> findTopMerchants();

}
