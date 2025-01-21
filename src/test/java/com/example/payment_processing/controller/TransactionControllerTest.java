package com.example.payment_processing.controller;

import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction(150.0);
        transaction.setId(1L);
        transaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void testCreateTransaction_Success() {
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(150.0, response.getBody().getTransactionAmount());
    }

    @Test
    void testGetTransaction_Success() {
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransaction(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(150.0, response.getBody().getTransactionAmount());
    }

    @Test
    void testGetAllTransactions_Success() {
        when(transactionService.getAllTransactions()).thenReturn(List.of(transaction));

        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateTransaction_Success() {
        when(transactionService.updateTransaction(eq(1L), any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(1L, transaction);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(150.0, response.getBody().getTransactionAmount());
    }

    @Test
    void testDeleteTransaction_Success() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(transactionService, times(1)).deleteTransaction(1L);
    }
}
