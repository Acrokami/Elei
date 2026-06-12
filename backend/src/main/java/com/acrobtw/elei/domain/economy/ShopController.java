package com.acrobtw.elei.domain.economy;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "In-game shop for purchasing items with money earned through gameplay")
public class ShopController {
    private final ShopService shopService;

    @Operation(summary = "Purchase item", description = "Allows the authenticated user to purchase an item from the shop using their in-game currency balance. The item is transferred to the user's inventory upon successful purchase.")
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
