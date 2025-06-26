package com.example.payment_processing.controller;

import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invoices")
public class InvoiceMvcController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceMvcController.class);

    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    public InvoiceMvcController(InvoiceService invoiceService,
                                CustomerService customerService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        log.info("Listing all invoices");
        var invoices = invoiceService.getAllInvoices();
        log.debug("Found {} invoices", invoices.size());
        model.addAttribute("invoices", invoices);
        return "invoices/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new invoice");
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("customers", customerService.getAllCustomers());
        return "invoices/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("invoice") Invoice invoice,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            log.warn("Validation errors when creating invoice: {}", br.getAllErrors());
            model.addAttribute("customers", customerService.getAllCustomers());
            return "invoices/form";
        }
        log.info("Creating invoice for customer id={} with amount {}",
                invoice.getCustomer().getId(), invoice.getTotalAmount());
        Invoice saved = invoiceService.createInvoice(invoice);
        log.debug("Created invoice id={}", saved.getId());
        return "redirect:/invoices";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Getting detail for invoice id={}", id);
        Invoice invoice = invoiceService.getInvoiceById(id);
        log.debug("Loaded invoice: {} for customer id={}", id, invoice.getCustomer().getId());
        model.addAttribute("invoice", invoice);
        return "invoices/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for invoice id={}", id);
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        model.addAttribute("customers", customerService.getAllCustomers());
        return "invoices/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("invoice") Invoice invoice,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            log.warn("Validation errors when updating invoice id={}: {}", id, br.getAllErrors());
            model.addAttribute("customers", customerService.getAllCustomers());
            return "invoices/form";
        }
        log.info("Updating invoice id={} to customer id={} with amount {}",
                id, invoice.getCustomer().getId(), invoice.getTotalAmount());
        Invoice updated = invoiceService.updateInvoice(id, invoice);
        log.debug("Updated invoice id={}", updated.getId());
        return "redirect:/invoices";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting invoice id={}", id);
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices";
    }
}