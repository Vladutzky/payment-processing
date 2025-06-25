package com.example.payment_processing.controller;

import com.example.payment_processing.model.Invoice;
import com.example.payment_processing.service.CustomerService;
import com.example.payment_processing.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invoices")
public class InvoiceMvcController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    public InvoiceMvcController(InvoiceService invoiceService,
                                CustomerService customerService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "invoices/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("customers", customerService.getAllCustomers());
        return "invoices/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("invoice") Invoice invoice,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            return "invoices/form";
        }
        invoiceService.createInvoice(invoice);
        return "redirect:/invoices";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        return "invoices/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
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
            model.addAttribute("customers", customerService.getAllCustomers());
            return "invoices/form";
        }
        invoiceService.updateInvoice(id, invoice);
        return "redirect:/invoices";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices";
    }
}
