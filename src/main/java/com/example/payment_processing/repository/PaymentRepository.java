package com.example.payment_processing.repository;

import com.example.payment_processing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.merchant.id = :merchantId")
    List<Payment> findByMerchantId(Long merchantId);
}
