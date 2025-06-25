package com.example.payment_processing.controller;

import com.example.payment_processing.model.Transaction;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import com.example.payment_processing.service.MerchantService;
import com.example.payment_processing.service.PaymentService;
import com.example.payment_processing.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionMvcController {

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
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "transactions/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("invoices",  invoiceService.getAllInvoices());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("payments",  paymentService.getAllPayments());
        return "transactions/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("transaction") Transaction txn,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("invoices",  invoiceService.getAllInvoices());
            model.addAttribute("merchants", merchantService.getAllMerchants());
            model.addAttribute("payments",  paymentService.getAllPayments());
            return "transactions/form";
        }
        transactionService.createTransaction(txn);
        return "redirect:/transactions";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("transaction", transactionService.getTransactionById(id));
        return "transactions/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("transaction", transactionService.getTransactionById(id));
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("invoices",  invoiceService.getAllInvoices());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("payments",  paymentService.getAllPayments());
        return "transactions/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("transaction") Transaction txn,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("invoices",  invoiceService.getAllInvoices());
            model.addAttribute("merchants", merchantService.getAllMerchants());
            model.addAttribute("payments",  paymentService.getAllPayments());
            return "transactions/form";
        }
        transactionService.updateTransaction(id, txn);
        return "redirect:/transactions";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "redirect:/transactions";
    }
}
