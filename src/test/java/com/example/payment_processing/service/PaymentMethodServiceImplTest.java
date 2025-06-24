package com.example.payment_processing.service;

import com.example.payment_processing.model.Payment;
import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.repository.PaymentMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
public class PaymentMethodServiceImplTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentMethod = new PaymentMethod("Credit Card");
        paymentMethod.setId(1L);
    }

    @Test
    void testCreatePaymentMethod_Success() {
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethod createdMethod = paymentMethodService.createPaymentMethod(paymentMethod);

        assertNotNull(createdMethod);
        assertEquals("Credit Card", createdMethod.getMethodName());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testGetPaymentMethodById_Success() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));

        PaymentMethod foundMethod = paymentMethodService.getPaymentMethodById(1L);

        assertNotNull(foundMethod);
        assertEquals("Credit Card", foundMethod.getMethodName());
    }

    @Test
    void testGetPaymentMethodById_NotFound() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentMethodService.getPaymentMethodById(1L);
        });

        assertTrue(exception.getMessage().contains("Payment Method not found"));
    }

    @Test
    void testGetAllPaymentMethods_Success() {
        when(paymentMethodRepository.findAll()).thenReturn(List.of(paymentMethod));

        List<PaymentMethod> methods = paymentMethodService.getAllPaymentMethods();

        assertNotNull(methods);
        assertEquals(1, methods.size());
    }

    @Test
    void testUpdatePaymentMethod_Success() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        PaymentMethod updatedMethod = new PaymentMethod("Debit Card");

        PaymentMethod result = paymentMethodService.updatePaymentMethod(1L, updatedMethod);

        assertNotNull(result);
        assertEquals("Debit Card", result.getMethodName());
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testDeletePaymentMethod_Success() {
        doNothing().when(paymentMethodRepository).deleteById(1L);

        assertDoesNotThrow(() -> paymentMethodService.deletePaymentMethod(1L));
        verify(paymentMethodRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCountPaymentsByMethod_Success() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        paymentMethod.getPayments().add(new Payment("John Doe", 100.0));
        paymentMethod.getPayments().add(new Payment("Jane Doe", 200.0));

        long count = paymentMethodService.countPaymentsByMethod(1L);

        assertEquals(2, count);
    }
}
