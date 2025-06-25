package com.example.payment_processing.controller;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/customers")
public class CustomerMvcController {

    private final CustomerService customerService;

    public CustomerMvcController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ——— LIST & SORT ——————————————————————————————————————————
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        List<Customer> list = customerService.getAllCustomersSorted(
                sortField, sortDir.toUpperCase(Locale.ROOT)
        );
        model.addAttribute("customers", list);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "customers/list";
    }

    // ——— FILTERS —————————————————————————————————————————————
    @GetMapping("/with-invoices")
    public String withInvoices(Model model) {
        var list = customerService.getAllCustomersSorted("id", "ASC")
                .stream()
                .filter(c -> !c.getInvoices().isEmpty())
                .toList();
        model.addAttribute("customers", list);
        model.addAttribute("title", "Customers WITH Invoices");
        return "customers/list";
    }

    @GetMapping("/with-transactions")
    public String withTransactions(Model model) {
        var list = customerService.getAllCustomersSorted("id", "ASC")
                .stream()
                .filter(c -> !c.getTransactions().isEmpty())
                .toList();
        model.addAttribute("customers", list);
        model.addAttribute("title", "Customers WITH Transactions");
        return "customers/list";
    }

    @GetMapping("/top-spender")
    public String topSpender(Model model) {
        Customer top = customerService.getTopSpendingCustomer();
        model.addAttribute("customers", List.of(top));
        model.addAttribute("title", "Top Spending Customer");
        return "customers/list";
    }

    // ——— DETAIL (with total spending) —————————————————————————
    @GetMapping("/{id:\\d+}")
    public String detail(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        model.addAttribute(
                "totalSpending",
                customerService.getTotalSpendingByCustomerId(id)
        );
        return "customers/detail";
    }

    // ——— CREATE ————————————————————————————————————————————————
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customerRequest", new Customer());
        return "customers/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("customerRequest") Customer customerRequest,
            BindingResult binding
    ) {
        if (binding.hasErrors()) {
            return "customers/form";
        }
        customerService.createCustomer(customerRequest);
        return "redirect:/customers";
    }

    // ——— EDIT ————————————————————————————————————————————————
    @GetMapping("/{id:\\d+}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customerRequest", customerService.getCustomerById(id));
        return "customers/form";
    }

    @PostMapping("/{id:\\d+}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("customerRequest") Customer customerRequest,
            BindingResult binding
    ) {
        if (binding.hasErrors()) {
            return "customers/form";
        }
        customerService.updateCustomer(id, customerRequest);
        return "redirect:/customers";
    }

    // ——— DELETE ———————————————————————————————————————————————
    @PostMapping("/{id:\\d+}/delete")
    public String delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
