package com.example.payment_processing.controller;

import com.example.payment_processing.model.Payment;
import com.example.payment_processing.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment("John Doe", 100.0);
        payment.setId(1L);
    }

    @Test
    void testCreatePayment_Success() {
        when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.createPayment(payment);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getPayerName());
    }

    @Test
    void testGetPayment_Success() {
        when(paymentService.getPaymentById(1L)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.getPayment(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getPayerName());
    }

    @Test
    void testGetAllPayments_Success() {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testRefundPayment_Success() {
        doNothing().when(paymentService).refundPayment(1L);
        when(paymentService.getPaymentById(1L)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.refundPayment(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(paymentService, times(1)).refundPayment(1L);
    }

    @Test
    void testGetPaymentMethodUsagePercentage_Success() {
        Map<String, Double> mockUsage = Map.of("Credit Card", 60.0, "PayPal", 40.0);
        when(paymentService.getPaymentMethodUsagePercentage(1L)).thenReturn(mockUsage);

        ResponseEntity<Map<String, Double>> response = paymentController.getPaymentMethodUsagePercentage(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(60.0, response.getBody().get("Credit Card"));
    }

    @Test
    void testUpdatePayment_Success() {
        when(paymentService.updatePayment(eq(1L), any(Payment.class))).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.updatePayment(1L, payment);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getPayerName());
    }

    @Test
    void testDeletePayment_Success() {
        doNothing().when(paymentService).deletePayment(1L);

        ResponseEntity<Void> response = paymentController.deletePayment(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(paymentService, times(1)).deletePayment(1L);
    }
}
