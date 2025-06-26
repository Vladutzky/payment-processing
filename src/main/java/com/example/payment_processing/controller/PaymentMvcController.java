package com.example.payment_processing.controller;

import com.example.payment_processing.model.Payment;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import com.example.payment_processing.service.MerchantService;
import com.example.payment_processing.service.PaymentMethodService;
import com.example.payment_processing.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payments")
public class PaymentMvcController {

    private static final Logger log = LoggerFactory.getLogger(PaymentMvcController.class);

    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final MerchantService merchantService;
    private final PaymentMethodService paymentMethodService;
    private final CustomerService customerService;

    public PaymentMvcController(
            PaymentService paymentService,
            InvoiceService invoiceService,
            MerchantService merchantService,
            PaymentMethodService paymentMethodService,
            CustomerService customerService
    ) {
        this.paymentService       = paymentService;
        this.invoiceService       = invoiceService;
        this.merchantService      = merchantService;
        this.paymentMethodService = paymentMethodService;
        this.customerService      = customerService;
    }

    // ——— MAIN LIST WITH SORTING —————————————————————————————————————————
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "id")  String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        log.info("Listing payments sorted by {} {}", sortField, sortDir);
        model.addAttribute("merchants", merchantService.getAllMerchants());
        List<Payment> payments = paymentService.getAllPaymentsSorted(sortField, sortDir);
        log.debug("Found {} payments", payments.size());
        model.addAttribute("payments", payments);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("title", "All Payments");
        return "payments/list";
    }

    // ——— FILTER BY MERCHANT ———————————————————————————————————————————————
    @GetMapping("/by-merchant/{merchantId}")
    public String listByMerchant(
            @PathVariable Long merchantId,
            @RequestParam(defaultValue = "id")  String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        log.info("Listing payments for merchant id={} sorted by {} {}", merchantId, sortField, sortDir);
        model.addAttribute("merchants", merchantService.getAllMerchants());
        List<Payment> sorted = paymentService.getAllPaymentsSorted(sortField, sortDir);
        List<Payment> payments = sorted.stream()
                .filter(p -> p.getMerchant() != null && p.getMerchant().getId().equals(merchantId))
                .collect(Collectors.toList());
        log.debug("Found {} payments for merchant id={}", payments.size(), merchantId);
        model.addAttribute("payments", payments);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedMerchantId", merchantId);
        String merchantName = merchantService.getMerchantById(merchantId).getMerchantName();
        model.addAttribute("title", "Payments for " + merchantName);
        return "payments/list";
    }

    // ——— CRUD ENDPOINTS ————————————————————————————————————————————————

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new payment");
        model.addAttribute("payment", new Payment());
        loadFormCollections(model);
        return "payments/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("payment") Payment payment,
            BindingResult br,
            Model model
    ) {
        if (br.hasErrors()) {
            log.warn("Validation errors when creating payment: {}", br.getAllErrors());
            loadFormCollections(model);
            return "payments/form";
        }
        log.info("Creating payment: payerName={} amount={} paymentDate={} invoiceId={} customerId={} merchantId={} methodId={}",
                payment.getPayerName(), payment.getAmount(), payment.getPaymentDate(),
                payment.getInvoice().getId(), payment.getCustomer().getId(),
                payment.getMerchant().getId(), payment.getPaymentMethod().getId());
        paymentService.createPayment(payment);
        return "redirect:/payments";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Getting detail for payment id={}", id);
        Payment payment = paymentService.getPaymentById(id);
        log.debug("Loaded payment id={} amount={}", id, payment.getAmount());
        model.addAttribute("payment", payment);
        return "payments/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for payment id={}", id);
        model.addAttribute("payment", paymentService.getPaymentById(id));
        loadFormCollections(model);
        return "payments/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("payment") Payment payment,
            BindingResult br,
            Model model
    ) {
        if (br.hasErrors()) {
            log.warn("Validation errors when updating payment id={}: {}", id, br.getAllErrors());
            loadFormCollections(model);
            return "payments/form";
        }
        log.info("Updating payment id={} to payerName={} amount={} paymentDate={} merchantId={} methodId={} invoiceId={} customerId={}",
                id, payment.getPayerName(), payment.getAmount(), payment.getPaymentDate(),
                payment.getMerchant().getId(), payment.getPaymentMethod().getId(),
                payment.getInvoice().getId(), payment.getCustomer().getId());
        paymentService.updatePayment(id, payment);
        log.debug("Updated payment id={}", id);
        return "redirect:/payments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting payment id={}", id);
        paymentService.deletePayment(id);
        return "redirect:/payments";
    }

    // ——— SHARED HELPER ————————————————————————————————————————————————
    private void loadFormCollections(Model model) {
        log.debug("Loading form collections for payments form");
        model.addAttribute("invoices",       invoiceService.getAllInvoices());
        model.addAttribute("merchants",      merchantService.getAllMerchants());
        model.addAttribute("paymentMethods", paymentMethodService.getAllPaymentMethods());
        model.addAttribute("customers",      customerService.getAllCustomers());
    }
}