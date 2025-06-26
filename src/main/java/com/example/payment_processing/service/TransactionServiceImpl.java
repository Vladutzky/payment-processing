package com.example.payment_processing.service;

import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.repository.TransactionRepository;
import com.example.payment_processing.model.Customer;
import com.example.payment_processing.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        log.info("Saving new transaction: amount={} date={}", transaction.getTransactionAmount(), transaction.getTransactionDate());
        Transaction saved = transactionRepository.save(transaction);
        log.debug("Transaction saved with id={}", saved.getId());
        return saved;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        log.info("Retrieving transaction by id={}", id);
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction not found: {}", id);
                    return new RuntimeException("Transaction not found: " + id);
                });
        log.debug("Retrieved transaction amount={} date={}", txn.getTransactionAmount(), txn.getTransactionDate());
        return txn;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        log.info("Retrieving all transactions");
        List<Transaction> list = transactionRepository.findAll();
        log.debug("Retrieved {} transactions", list.size());
        return list;
    }

    @Override
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        log.info("Updating transaction id={}", id);
        Transaction existing = getTransactionById(id);
        existing.setTransactionAmount(updatedTransaction.getTransactionAmount());
        existing.setTransactionDate(updatedTransaction.getTransactionDate());
        Transaction saved = transactionRepository.save(existing);
        log.debug("Transaction id={} updated to amount={} date={}", id, saved.getTransactionAmount(), saved.getTransactionDate());
        return saved;
    }

    @Override
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction id={}", id);
        transactionRepository.deleteById(id);
        log.debug("Deleted transaction id={}", id);
    }

    @Override
    public List<Transaction> getTransactionsByCustomer(Long customerId) {
        log.info("Retrieving transactions for customer id={}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found: {}", customerId);
                    return new RuntimeException("Customer not found: " + customerId);
                });
        List<Transaction> list = customer.getTransactions();
        log.debug("Retrieved {} transactions for customer id={}", list.size(), customerId);
        return list;
    }
}
