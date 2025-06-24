package com.example.payment_processing.controller;

import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
public class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentMethod = new PaymentMethod("Credit Card");
        paymentMethod.setId(1L);
    }

    @Test
    void testCreatePaymentMethod_Success() {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethod.class))).thenReturn(paymentMethod);

        ResponseEntity<PaymentMethod> response = paymentMethodController.createPaymentMethod(paymentMethod);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Credit Card", response.getBody().getMethodName());
    }

    @Test
    void testGetPaymentMethod_Success() {
        when(paymentMethodService.getPaymentMethodById(1L)).thenReturn(paymentMethod);

        ResponseEntity<PaymentMethod> response = paymentMethodController.getPaymentMethod(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Credit Card", response.getBody().getMethodName());
    }

    @Test
    void testGetAllPaymentMethods_Success() {
        when(paymentMethodService.getAllPaymentMethods()).thenReturn(List.of(paymentMethod));

        ResponseEntity<List<PaymentMethod>> response = paymentMethodController.getAllPaymentMethods();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdatePaymentMethod_Success() {
        when(paymentMethodService.updatePaymentMethod(eq(1L), any(PaymentMethod.class))).thenReturn(paymentMethod);

        ResponseEntity<PaymentMethod> response = paymentMethodController.updatePaymentMethod(1L, paymentMethod);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Credit Card", response.getBody().getMethodName());
    }

    @Test
    void testDeletePaymentMethod_Success() {
        doNothing().when(paymentMethodService).deletePaymentMethod(1L);

        ResponseEntity<Void> response = paymentMethodController.deletePaymentMethod(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(paymentMethodService, times(1)).deletePaymentMethod(1L);
    }
}
