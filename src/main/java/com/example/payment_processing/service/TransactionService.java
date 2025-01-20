package com.example.payment_processing.service;

import com.example.payment_processing.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    Transaction getTransactionById(Long id);

    List<Transaction> getAllTransactions();

    Transaction updateTransaction(Long id, Transaction transaction);

    void deleteTransaction(Long id);

    List<Transaction> getTransactionsByCustomer(Long customerId);
}
