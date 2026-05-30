package com.acrobtw.elei.domain.user;

import com.acrobtw.elei.domain.auth.enums.AuthProvider;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    @Modifying
    @Query("UPDATE User u SET u.currentStreak = 0 WHERE u.lastActivityDate < :yesterday AND u.currentStreak > 0")
    void resetBrokenStreaks(@Param("yesterday") LocalDate yesterday);

}
