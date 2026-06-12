package com.acrobtw.elei.domain.economy;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @PostMapping("/purchase/{itemId}")
    public ResponseEntity<?> purchaseItem(Principal principal, @PathVariable Long itemId) {
        try {
            shopService.purchaseItem(principal.getName(), itemId);
            return ResponseEntity.ok().body("Transaction authorized. Asset transferred to inventory.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
