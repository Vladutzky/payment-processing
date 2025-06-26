package com.example.payment_processing.controller;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/customers")
public class CustomerMvcController {

    private static final Logger log = LoggerFactory.getLogger(CustomerMvcController.class);

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
        log.info("Listing customers sorted by {} {}", sortField, sortDir);
        List<Customer> list = customerService.getAllCustomersSorted(
                sortField, sortDir.toUpperCase(Locale.ROOT)
        );
        log.debug("Found {} customers", list.size());
        model.addAttribute("customers", list);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "customers/list";
    }

    // ——— FILTERS —————————————————————————————————————————————
    @GetMapping("/with-invoices")
    public String withInvoices(Model model) {
        log.info("Filtering customers WITH invoices");
        var list = customerService.getAllCustomersSorted("id", "ASC")
                .stream()
                .filter(c -> !c.getInvoices().isEmpty())
                .toList();
        log.debug("{} customers have invoices", list.size());
        model.addAttribute("customers", list);
        model.addAttribute("title", "Customers WITH Invoices");
        return "customers/list";
    }

    @GetMapping("/with-transactions")
    public String withTransactions(Model model) {
        log.info("Filtering customers WITH transactions");
        var list = customerService.getAllCustomersSorted("id", "ASC")
                .stream()
                .filter(c -> !c.getTransactions().isEmpty())
                .toList();
        log.debug("{} customers have transactions", list.size());
        model.addAttribute("customers", list);
        model.addAttribute("title", "Customers WITH Transactions");
        return "customers/list";
    }

    @GetMapping("/top-spender")
    public String topSpender(Model model) {
        log.info("Getting top spending customer");
        Customer top = customerService.getTopSpendingCustomer();
        log.debug("Top spender is id={} email={} ", top.getId(), top.getCustomerEmail());
        model.addAttribute("customers", List.of(top));
        model.addAttribute("title", "Top Spending Customer");
        return "customers/list";
    }

    // ——— DETAIL (with total spending) —————————————————————————
    @GetMapping("/{id:\\d+}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Showing detail for customer id={}", id);
        Customer customer = customerService.getCustomerById(id);
        double total = customerService.getTotalSpendingByCustomerId(id);
        log.debug("Total spending for customer id={} is {}", id, total);
        model.addAttribute("customer", customer);
        model.addAttribute("totalSpending", total);
        return "customers/detail";
    }

    // ——— CREATE ————————————————————————————————————————————————
    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new customer");
        model.addAttribute("customerRequest", new Customer());
        return "customers/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("customerRequest") Customer customerRequest,
            BindingResult binding
    ) {
        if (binding.hasErrors()) {
            log.warn("Validation errors when creating customer: {}", binding.getAllErrors());
            return "customers/form";
        }
        log.info("Creating customer with email {}", customerRequest.getCustomerEmail());
        customerService.createCustomer(customerRequest);
        return "redirect:/customers";
    }

    // ——— EDIT ————————————————————————————————————————————————
    @GetMapping("/{id:\\d+}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for customer id={}", id);
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
            log.warn("Validation errors when updating customer id={}: {}", id, binding.getAllErrors());
            return "customers/form";
        }
        log.info("Updating customer id={}", id);
        customerService.updateCustomer(id, customerRequest);
        log.debug("Updated customer id={}", id);
        return "redirect:/customers";
    }

    // ——— DELETE ———————————————————————————————————————————————
    @PostMapping("/{id:\\d+}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting customer id={}", id);
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}