package com.acrobtw.elei.domain.economy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acrobtw.elei.domain.economy.Wallet;

import jakarta.persistence.LockModeType;

public interface WalletRepository extends JpaRepository<Wallet, Long>{


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.username = :username")
    Optional<Wallet>findByUserUsernameForUpdate(@Param("username") String username);

    Optional<Wallet> findByUserUsername(String username);
}
