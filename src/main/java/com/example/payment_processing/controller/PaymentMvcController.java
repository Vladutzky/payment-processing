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

@Controller
@RequestMapping("/payments")
public class PaymentMvcController {

    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final MerchantService merchantService;
    private final PaymentMethodService paymentMethodService;
    private final CustomerService customerService;

    public PaymentMvcController(PaymentService paymentService,
                                InvoiceService invoiceService,
                                MerchantService merchantService,
                                PaymentMethodService paymentMethodService,
                                CustomerService customerService) {
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.merchantService = merchantService;
        this.paymentMethodService = paymentMethodService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("payment", new Payment());
        loadFormCollections(model);
        return "payments/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("payment") Payment payment,
                         BindingResult br,
                         Model model) {
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
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("payment") Payment payment,
                         BindingResult br,
                         Model model) {
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

    /**
     * Helper to populate all dropdown lists needed by the form.
     */
    private void loadFormCollections(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("merchants", merchantService.getAllMerchants());
        model.addAttribute("paymentMethods", paymentMethodService.getAllPaymentMethods());
        model.addAttribute("customers", customerService.getAllCustomers());
    }
}
