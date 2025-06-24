package com.example.payment_processing.controller;

import com.example.payment_processing.model.Customer;
import com.example.payment_processing.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerMvcController {

    private final CustomerService customerService;

    public CustomerMvcController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 1) LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    // 2) DETAIL
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "customers/detail";
    }

    // 3) NEW FORM
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customerRequest", new Customer());
        return "customers/form";
    }

    // 4) CREATE
    @PostMapping
    public String create(@Valid @ModelAttribute("customerRequest") Customer customerRequest,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            return "customers/form";
        }
        customerService.createCustomer(customerRequest);
        return "redirect:/customers";
    }

    // 5) EDIT FORM
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customerRequest", customerService.getCustomerById(id));
        return "customers/form";
    }

    // 6) UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("customerRequest") Customer customerRequest,
                         BindingResult binding) {
        if (binding.hasErrors()) {
            return "customers/form";
        }
        customerService.updateCustomer(id, customerRequest);
        return "redirect:/customers";
    }

    // (optionally) 7) DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
