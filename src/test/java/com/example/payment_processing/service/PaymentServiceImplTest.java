package com.example.payment_processing.service;

import com.example.payment_processing.model.*;
import com.example.payment_processing.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer("John Doe", "john.doe@example.com");
        customer.setId(1L);

        payment = new Payment("John Doe", 100.0);
        payment.setId(1L);
        payment.setCustomer(customer);
    }

    @Test
    void testCreatePayment_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment createdPayment = paymentService.createPayment(payment);

        assertNotNull(createdPayment);
        assertEquals("John Doe", createdPayment.getPayerName());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePayment_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(payment);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void testGetPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Payment foundPayment = paymentService.getPaymentById(1L);

        assertNotNull(foundPayment);
        assertEquals("John Doe", foundPayment.getPayerName());
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentById(1L);
        });

        assertTrue(exception.getMessage().contains("Payment not found"));
    }

    @Test
    void testRefundPayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        paymentService.refundPayment(1L);

        assertTrue(payment.isRefunded());
        verify(paymentRepository, times(2)).save(any(Payment.class)); // Original and refund
    }

    @Test
    void testRefundPayment_AlreadyRefunded() {
        payment.setRefunded(true);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.refundPayment(1L);
        });

        assertTrue(exception.getMessage().contains("Payment has already been refunded"));
    }

    @Test
    void testGetAllPayments_Success() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<Payment> payments = paymentService.getAllPayments();

        assertNotNull(payments);
        assertEquals(1, payments.size());
    }

    @Test
    void testUpdatePayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = new Payment("Jane Doe", 200.0);
        updatedPayment.setPaymentDate(payment.getPaymentDate());

        Payment result = paymentService.updatePayment(1L, updatedPayment);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getPayerName());
        assertEquals(200.0, result.getAmount());
    }

    @Test
    void testDeletePayment_Success() {
        doNothing().when(paymentRepository).deleteById(1L);

        assertDoesNotThrow(() -> paymentService.deletePayment(1L));
        verify(paymentRepository, times(1)).deleteById(1L);
    }
}
