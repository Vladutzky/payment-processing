package com.example.payment_processing.repository;

import com.example.payment_processing.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c.id, c.customerName, SUM(p.amount) AS totalSpent FROM Payment p JOIN p.customer c GROUP BY c.id ORDER BY totalSpent DESC")
    List<Object[]> findTopSpendingCustomer();
}
