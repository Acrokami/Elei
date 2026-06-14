package com.acrobtw.elei.domain.economy;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acrobtw.elei.domain.economy.repository.InventoryItemRepository;
import com.acrobtw.elei.domain.economy.repository.ShopItemRepository;
import com.acrobtw.elei.domain.economy.repository.WalletRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopService {

    private final WalletRepository walletRepository;
    private final ShopItemRepository shopItemRepository;
    private final InventoryItemRepository inventoryRepository;


    @Transactional
    public void purchaseItem(String username, Long itemId) {
        log.info("[ECONOMY] Initiating purchase sequence for user: {}, item ID: {}", username, itemId);

        Wallet wallet = walletRepository.findByUserUsernameForUpdate(username)
                .orElseThrow(() -> new IllegalArgumentException("Financial node not found for user: " + username));

        ShopItem item = shopItemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Target item not found in database."));

        if (item.getStock() <= 0) {
            throw new IllegalStateException("Operation aborted: Target item is out of stock.");
        }


        if (wallet.getBalance() < item.getPrice()) {
            throw new IllegalStateException("Operation aborted: Insufficient credits. Required: " + item.getPrice());
        }

        wallet.setBalance(wallet.getBalance() - item.getPrice());
        item.setStock(item.getStock() - 1);

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setUser(wallet.getUser());
        inventoryItem.setShopItem(item);

        walletRepository.save(wallet);
        shopItemRepository.save(item);
        inventoryRepository.save(inventoryItem);

        log.info("[SYSTEM] Purchase successful. User {} bought {} for {} credits.", username, item.getTitle(), item.getPrice());
    }


    @Transactional
    public ShopItem addShopItem(ShopItem item) {
        log.info("[ADMIN] Adding new item to market: {}", item.getTitle());
        return shopItemRepository.save(item);
    }


    @Transactional(readOnly = true)
    public Long getWalletBalance(String username) {
        return walletRepository.findByUserUsernameForUpdate(username)
                .map(Wallet::getBalance)
                .orElse(0L);
    }

    @Transactional(readOnly = true)
    public List<ShopItem> getAvailableItems() {
        return shopItemRepository.findAll();
    }
}
