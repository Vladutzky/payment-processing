package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.InvoiceRepository;
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
public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

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
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice createdInvoice = invoiceService.createInvoice(invoice);

        assertNotNull(createdInvoice);
        assertEquals(100.0, createdInvoice.getTotalAmount());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testCreateInvoice_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            invoiceService.createInvoice(invoice);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void testGetInvoiceById_Success() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.getInvoiceById(1L);

        assertNotNull(foundInvoice);
        assertEquals(100.0, foundInvoice.getTotalAmount());
    }

    @Test
    void testGetInvoiceById_NotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            invoiceService.getInvoiceById(1L);
        });

        assertTrue(exception.getMessage().contains("Invoice not found"));
    }

    @Test
    void testGetAllInvoices_Success() {
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        List<Invoice> invoices = invoiceService.getAllInvoices();

        assertNotNull(invoices);
        assertEquals(1, invoices.size());
    }

    @Test
    void testGetInvoicesByCustomerId_Success() {
        when(invoiceRepository.findByCustomerId(1L)).thenReturn(List.of(invoice));

        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(1L);

        assertNotNull(invoices);
        assertEquals(1, invoices.size());
    }

    @Test
    void testUpdateInvoice_Success() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice updatedInvoice = new Invoice(200.0);
        updatedInvoice.setCustomer(customer);

        Invoice result = invoiceService.updateInvoice(1L, updatedInvoice);

        assertNotNull(result);
        assertEquals(200.0, result.getTotalAmount());
    }

    @Test
    void testDeleteInvoice_Success() {
        doNothing().when(invoiceRepository).deleteById(1L);

        assertDoesNotThrow(() -> invoiceService.deleteInvoice(1L));
        verify(invoiceRepository, times(1)).deleteById(1L);
    }
}
