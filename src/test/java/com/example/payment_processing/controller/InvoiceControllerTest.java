package com.example.payment_processing.controller;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
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
public class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private InvoiceController invoiceController;

    private Invoice invoice;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer("John Doe", "john.doe@example.com");
        customer.setId(1L);

        invoice = new Invoice(100.0);
        invoice.setId(1L);
        invoice.setCustomer(customer);
    }

    @Test
    void testCreateInvoice_Success() {
        when(invoiceService.createInvoice(any(Invoice.class))).thenReturn(invoice);

        ResponseEntity<Invoice> response = invoiceController.createInvoice(invoice);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100.0, response.getBody().getTotalAmount());
    }

    @Test
    void testGetInvoice_Success() {
        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice);

        ResponseEntity<Invoice> response = invoiceController.getInvoice(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100.0, response.getBody().getTotalAmount());
    }

    @Test
    void testGetAllInvoices_Success() {
        when(invoiceService.getAllInvoices()).thenReturn(List.of(invoice));

        ResponseEntity<List<Invoice>> response = invoiceController.getAllInvoices();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetInvoicesByCustomerId_Success() {
        when(invoiceService.getInvoicesByCustomerId(1L)).thenReturn(List.of(invoice));

        ResponseEntity<List<Invoice>> response = invoiceController.getInvoicesByCustomerId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateInvoice_Success() {
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(invoiceService.updateInvoice(eq(1L), any(Invoice.class))).thenReturn(invoice);

        ResponseEntity<Invoice> response = invoiceController.updateInvoice(1L, invoice);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100.0, response.getBody().getTotalAmount());
    }

    @Test
    void testDeleteInvoice_Success() {
        doNothing().when(invoiceService).deleteInvoice(1L);

        ResponseEntity<Void> response = invoiceController.deleteInvoice(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(invoiceService, times(1)).deleteInvoice(1L);
    }
}
