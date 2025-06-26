package com.example.payment_processing.controller;

import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import com.example.payment_processing.service.MerchantService;
import com.example.payment_processing.service.PaymentService;
import com.example.payment_processing.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionMvcController {

    private static final Logger log = LoggerFactory.getLogger(TransactionMvcController.class);

    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final MerchantService merchantService;
    private final PaymentService paymentService;

    public TransactionMvcController(TransactionService transactionService,
                                    CustomerService customerService,
                                    InvoiceService invoiceService,
                                    MerchantService merchantService,
                                    PaymentService paymentService) {
        this.transactionService = transactionService;
        this.customerService    = customerService;
        this.invoiceService     = invoiceService;
        this.merchantService    = merchantService;
        this.paymentService     = paymentService;
    }

    @GetMapping
    public String list(Model model) {
        log.info("Listing all transactions");
        var transactions = transactionService.getAllTransactions();
        log.debug("Found {} transactions", transactions.size());
        model.addAttribute("transactions", transactions);
        return "transactions/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new transaction");
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("invoices",  invoiceService.getAllInvoices());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("payments",  paymentService.getAllPayments());
        log.debug("Loaded form collections for transactions");
        return "transactions/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("transaction") Transaction txn,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            log.warn("Validation errors when creating transaction: {}", br.getAllErrors());
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("invoices",  invoiceService.getAllInvoices());
            model.addAttribute("merchants", merchantService.getAllMerchants());
            model.addAttribute("payments",  paymentService.getAllPayments());
            return "transactions/form";
        }
        log.info("Creating transaction: amount={} on date={} for customerId={} invoiceId={} merchantId={} paymentId={}",
                txn.getTransactionAmount(), txn.getTransactionDate(),
                txn.getCustomer().getId(), txn.getInvoice().getId(),
                txn.getMerchant().getId(), txn.getPayment().getId());
        transactionService.createTransaction(txn);
        log.debug("Transaction created successfully");
        return "redirect:/transactions";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Getting detail for transaction id={}", id);
        var transaction = transactionService.getTransactionById(id);
        log.debug("Loaded transaction amount={} date={}", transaction.getTransactionAmount(), transaction.getTransactionDate());
        model.addAttribute("transaction", transaction);
        return "transactions/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for transaction id={}", id);
        model.addAttribute("transaction", transactionService.getTransactionById(id));
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("invoices",  invoiceService.getAllInvoices());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("payments",  paymentService.getAllPayments());
        log.debug("Loaded existing transaction and form collections for edit");
        return "transactions/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("transaction") Transaction txn,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            log.warn("Validation errors when updating transaction id={}: {}", id, br.getAllErrors());
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("invoices",  invoiceService.getAllInvoices());
            model.addAttribute("merchants", merchantService.getAllMerchants());
            model.addAttribute("payments",  paymentService.getAllPayments());
            return "transactions/form";
        }
        log.info("Updating transaction id={} to amount={} date={}", id, txn.getTransactionAmount(), txn.getTransactionDate());
        transactionService.updateTransaction(id, txn);
        log.debug("Transaction id={} updated successfully", id);
        return "redirect:/transactions";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting transaction id={}", id);
        transactionService.deleteTransaction(id);
        log.debug("Transaction id={} deleted", id);
        return "redirect:/transactions";
    }
}