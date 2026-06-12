package com.acrobtw.elei.domain.economy;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acrobtw.elei.domain.economy.repository.WalletRepository;
import com.acrobtw.elei.domain.user.event.UserRegisteredEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletInitializer {

    private final WalletRepository walletRepository;
    private static final Long STARTING_BALANCE = 500L;

    @EventListener
    @Transactional
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("[ECONOMY] Triggered financial allocation protocol for user: {}", event.user().getUsername());

        if (walletRepository.findByUserUsernameForUpdate(event.user().getUsername()).isPresent()) {
            log.warn("[ECONOMY] Wallet already exists for citizen: {}. Aborting allocation.", event.user().getUsername());
            return;
        }

        Wallet wallet = new Wallet();
        wallet.setUser(event.user());
        wallet.setBalance(STARTING_BALANCE);

        walletRepository.save(wallet);
        log.info("[SYSTEM] Wallet successfully provisioned for {}. Allocated: {} credits.",
                event.user().getUsername(), STARTING_BALANCE);
    }
}
