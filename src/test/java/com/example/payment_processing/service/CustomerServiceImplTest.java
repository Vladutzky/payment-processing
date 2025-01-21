package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("John Doe");
        customer.setCustomerEmail("john.doe@example.com");

        Invoice invoice = new Invoice();
        Payment payment = new Payment();
        payment.setAmount(100.0);
        invoice.setPayments(Collections.singletonList(payment));
        customer.setInvoices(Collections.singletonList(invoice));
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customer);

        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.getCustomerName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(1L);

        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getCustomerName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(1L);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCustomers_Success() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        List<Customer> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerName("Jane Doe");
        updatedCustomer.setCustomerEmail("jane.doe@example.com");

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getCustomerName());
        assertEquals("jane.doe@example.com", result.getCustomerEmail());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(1L);

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
        verify(customerRepository, times(1)).deleteById(1L);
    }



    @Test
    void testGetTotalSpendingByCustomerId_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Double totalSpending = customerService.getTotalSpendingByCustomerId(1L);

        assertNotNull(totalSpending);
        assertEquals(100.0, totalSpending);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomersWithInvoices_Success() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        List<Customer> customersWithInvoices = customerService.getCustomersWithInvoices();

        assertNotNull(customersWithInvoices);
        assertEquals(1, customersWithInvoices.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetTopSpendingCustomer_Success() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        Customer topSpender = customerService.getTopSpendingCustomer();

        assertNotNull(topSpender);
        assertEquals("John Doe", topSpender.getCustomerName());
        verify(customerRepository, times(1)).findAll();
    }
}
