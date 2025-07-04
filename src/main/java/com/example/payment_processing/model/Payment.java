package com.example.payment_processing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Schema(name = "Payment", description = "Represents a payment entity in the system")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String payerName;

    @NotNull @Positive
    private Double amount;

    private boolean refunded = false;

    @NotNull
    private LocalDateTime paymentDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "invoice_id")
    @NotNull
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "merchant_id")
    @NotNull
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "payment_method_id")
    @NotNull
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id")
    @NotNull
    private Customer customer;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Transaction transaction;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public boolean isRefunded() { return refunded; }
    public void setRefunded(boolean refunded) { this.refunded = refunded; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public Merchant getMerchant() { return merchant; }
    public void setMerchant(Merchant merchant) { this.merchant = merchant; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
}
