package com.example.payment_processing.controller;

import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/merchants")
public class MerchantMvcController {

    private final MerchantService merchantService;

    public MerchantMvcController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("merchants", merchantService.getAllMerchants());
        return "merchants/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "merchants/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("merchant") Merchant merchant,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "merchants/form";
        }
        merchantService.createMerchant(merchant);
        return "redirect:/merchants";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("merchant", merchantService.getMerchantById(id));
        return "merchants/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("merchant", merchantService.getMerchantById(id));
        return "merchants/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("merchant") Merchant merchant,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "merchants/form";
        }
        merchantService.updateMerchant(id, merchant);
        return "redirect:/merchants";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return "redirect:/merchants";
    }
}
