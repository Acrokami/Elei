package com.acrobtw.elei.domain.economy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acrobtw.elei.domain.economy.ShopItem;

import jakarta.persistence.LockModeType;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShopItem s WHERE s.id = :id")
    Optional<ShopItem> findByIdForUpdate(@Param("id") Long id);
}
