package com.example.payment_processing.controller;

import com.example.payment_processing.model.Payment;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import com.example.payment_processing.service.MerchantService;
import com.example.payment_processing.service.PaymentMethodService;
import com.example.payment_processing.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payments")
public class PaymentMvcController {

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
            @RequestParam(defaultValue = "id")      String sortField,
            @RequestParam(defaultValue = "asc")     String sortDir,
            Model model
    ) {
        // load merchants for the filter dropdown
        model.addAttribute("merchants", merchantService.getAllMerchants());

        // fetch sorted payments
        List<Payment> payments = paymentService.getAllPaymentsSorted(sortField, sortDir);
        model.addAttribute("payments", payments);

        // for updating the table header links
        model.addAttribute("sortField",    sortField);
        model.addAttribute("sortDir",      sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("title", "All Payments");
        return "payments/list";
    }

    // ——— FILTER BY MERCHANT (still allows sorting) —————————————————————————
    @GetMapping("/by-merchant/{merchantId}")
    public String listByMerchant(
            @PathVariable Long merchantId,
            @RequestParam(defaultValue = "id")  String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        // always populate dropdown
        model.addAttribute("merchants", merchantService.getAllMerchants());
        // start from the fully sorted list, then filter
        List<Payment> sorted = paymentService.getAllPaymentsSorted(sortField, sortDir);
        List<Payment> payments = sorted.stream()
                .filter(p -> p.getMerchant() != null && p.getMerchant().getId().equals(merchantId))
                .collect(Collectors.toList());
        model.addAttribute("payments", payments);

        model.addAttribute("sortField",    sortField);
        model.addAttribute("sortDir",      sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedMerchantId", merchantId);

        String merchantName = merchantService.getMerchantById(merchantId).getMerchantName();
        model.addAttribute("title", "Payments for " + merchantName);
        return "payments/list";
    }

    // ——— CRUD ENDPOINTS ————————————————————————————————————————————————

    @GetMapping("/new")
    public String createForm(Model model) {
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
            loadFormCollections(model);
            return "payments/form";
        }
        paymentService.createPayment(payment);
        return "redirect:/payments";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("payment", paymentService.getPaymentById(id));
        return "payments/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
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
            loadFormCollections(model);
            return "payments/form";
        }
        paymentService.updatePayment(id, payment);
        return "redirect:/payments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return "redirect:/payments";
    }

    // ——— SHARED HELPER ————————————————————————————————————————————————
    private void loadFormCollections(Model model) {
        model.addAttribute("invoices",       invoiceService.getAllInvoices());
        model.addAttribute("merchants",      merchantService.getAllMerchants());
        model.addAttribute("paymentMethods", paymentMethodService.getAllPaymentMethods());
        model.addAttribute("customers",      customerService.getAllCustomers());
    }
}
