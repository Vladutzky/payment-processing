package com.example.payment_processing.repository;

import com.example.payment_processing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId")
    List<Invoice> findByCustomerId(Long customerId);
}
