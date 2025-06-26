package com.example.payment_processing.controller;

import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment-methods")
public class PaymentMethodMvcController {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodMvcController.class);

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodMvcController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public String list(Model model) {
        log.info("Listing all payment methods");
        var methods = paymentMethodService.getAllPaymentMethods();
        log.debug("Found {} payment methods", methods.size());
        model.addAttribute("methods", methods);
        return "payment-methods/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new payment method");
        model.addAttribute("method", new PaymentMethod());
        return "payment-methods/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("method") PaymentMethod method,
                         BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validation errors when creating payment method: {}", br.getAllErrors());
            return "payment-methods/form";
        }
        log.info("Creating payment method name={}", method.getMethodName());
        paymentMethodService.createPaymentMethod(method);
        return "redirect:/payment-methods";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Getting detail for payment method id={}", id);
        PaymentMethod method = paymentMethodService.getPaymentMethodById(id);
        log.debug("Loaded payment method name={}", method.getMethodName());
        model.addAttribute("method", method);
        return "payment-methods/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for payment method id={}", id);
        model.addAttribute("method", paymentMethodService.getPaymentMethodById(id));
        return "payment-methods/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("method") PaymentMethod method,
                         BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validation errors when updating payment method id={}: {}", id, br.getAllErrors());
            return "payment-methods/form";
        }
        log.info("Updating payment method id={} name={}", id, method.getMethodName());
        paymentMethodService.updatePaymentMethod(id, method);
        log.debug("Updated payment method id={}", id);
        return "redirect:/payment-methods";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting payment method id={}", id);
        paymentMethodService.deletePaymentMethod(id);
        return "redirect:/payment-methods";
    }
}
