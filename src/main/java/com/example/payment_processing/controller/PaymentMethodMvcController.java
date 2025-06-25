package com.example.payment_processing.controller;

import com.example.payment_processing.model.PaymentMethod;
import com.example.payment_processing.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment-methods")
public class PaymentMethodMvcController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodMvcController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("methods", paymentMethodService.getAllPaymentMethods());
        return "payment-methods/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("method", new PaymentMethod());
        return "payment-methods/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("method") PaymentMethod method,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "payment-methods/form";
        }
        paymentMethodService.createPaymentMethod(method);
        return "redirect:/payment-methods";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("method", paymentMethodService.getPaymentMethodById(id));
        return "payment-methods/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("method", paymentMethodService.getPaymentMethodById(id));
        return "payment-methods/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("method") PaymentMethod method,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "payment-methods/form";
        }
        paymentMethodService.updatePaymentMethod(id, method);
        return "redirect:/payment-methods";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return "redirect:/payment-methods";
    }
}
