package com.acrobtw.elei.domain.economy;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "Get wallet balance", description = "Returns the current in-game currency balance of the authenticated user.")
    @GetMapping("/wallet")
    public ResponseEntity<Long> getWalletBalance(Principal principal) {
        return ResponseEntity.ok(shopService.getWalletBalance(principal.getName()));
    }

    @Operation(summary = "Get available items", description = "Returns a list of items currently available for purchase in the shop, including their price and stock information.")
    @GetMapping("/items")
    public ResponseEntity<List<ShopItem>> getAvailableItems() {
        return ResponseEntity.ok(shopService.getAvailableItems());
    }

    @Operation(summary = "Add new item", description = "Allows an administrator to add a new item to the shop. The item will be available for purchase by all users.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopItem> addNewItem(@RequestBody ShopItem item) {
        return ResponseEntity.ok(shopService.addShopItem(item));
    }

}
