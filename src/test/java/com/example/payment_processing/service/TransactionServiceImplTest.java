package com.example.payment_processing.service;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.repository.CustomerRepository;
import com.example.payment_processing.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction(200.0);
        transaction.setId(1L);
        transaction.setTransactionDate(LocalDateTime.now());

        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("John Doe");
        customer.setTransactions(List.of(transaction));
    }

    @Test
    void testCreateTransaction_Success() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals(200.0, createdTransaction.getTransactionAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionById_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.getTransactionById(1L);

        assertNotNull(foundTransaction);
        assertEquals(200.0, foundTransaction.getTransactionAmount());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertTrue(exception.getMessage().contains("Transaction not found"));
    }

    @Test
    void testGetAllTransactions_Success() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    void testUpdateTransaction_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction updatedTransaction = new Transaction(300.0);
        updatedTransaction.setTransactionDate(LocalDateTime.now());

        Transaction result = transactionService.updateTransaction(1L, updatedTransaction);

        assertNotNull(result);
        assertEquals(300.0, result.getTransactionAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeleteTransaction_Success() {
        doNothing().when(transactionRepository).deleteById(1L);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(1L));
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTransactionsByCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        List<Transaction> transactions = transactionService.getTransactionsByCustomer(1L);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    void testGetTransactionsByCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionsByCustomer(1L);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
    }
}
