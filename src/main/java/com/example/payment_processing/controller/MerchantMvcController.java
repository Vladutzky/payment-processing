package com.example.payment_processing.controller;

import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.service.MerchantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/merchants")
public class MerchantMvcController {

    private static final Logger log = LoggerFactory.getLogger(MerchantMvcController.class);

    private final MerchantService merchantService;

    public MerchantMvcController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public String list(Model model) {
        log.info("Listing all merchants");
        var merchants = merchantService.getAllMerchants();
        log.debug("Found {} merchants", merchants.size());
        model.addAttribute("merchants", merchants);
        return "merchants/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        log.info("Opening form to create new merchant");
        model.addAttribute("merchant", new Merchant());
        return "merchants/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("merchant") Merchant merchant,
                         BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validation errors when creating merchant: {}", br.getAllErrors());
            return "merchants/form";
        }
        log.info("Creating merchant name={} email={}", merchant.getMerchantName(), merchant.getMerchantEmail());
        merchantService.createMerchant(merchant);
        return "redirect:/merchants";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("Getting detail for merchant id={}", id);
        Merchant merchant = merchantService.getMerchantById(id);
        log.debug("Loaded merchant name={} email={}", merchant.getMerchantName(), merchant.getMerchantEmail());
        model.addAttribute("merchant", merchant);
        return "merchants/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("Opening edit form for merchant id={}", id);
        model.addAttribute("merchant", merchantService.getMerchantById(id));
        return "merchants/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("merchant") Merchant merchant,
                         BindingResult br) {
        if (br.hasErrors()) {
            log.warn("Validation errors when updating merchant id={}: {}", id, br.getAllErrors());
            return "merchants/form";
        }
        log.info("Updating merchant id={} name={} email={}", id, merchant.getMerchantName(), merchant.getMerchantEmail());
        merchantService.updateMerchant(id, merchant);
        log.debug("Updated merchant id={}", id);
        return "redirect:/merchants";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting merchant id={}", id);
        merchantService.deleteMerchant(id);
        return "redirect:/merchants";
    }
}
