package com.acrobtw.elei.domain.user.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.leaderboard.LeaderboardService;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStreakService {
    private final UserRepository userRepository;
    private final LeaderboardService leaderboardService;

    @Transactional
    public String processDailyCheckIn(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        LocalDate today = LocalDate.now();
        if(today.equals(user.getLastActivityDate())) {
            return "The protocol is already activated today. Come back tomorrow for the continuation of the series.";
        }

        if(user.getLastActivityDate() != null
        && user.getLastActivityDate().isBefore(today.minusDays(1))) {
            user.setCurrentStreak(0);
        }

        user.setCurrentStreak(user.getCurrentStreak() + 1);
        user.setLastActivityDate(today);

        long bonusXp = 50L + (user.getCurrentStreak() * 10L);
        long currentXp = user.getTotalExperience() != null ? user.getTotalExperience() : 0L;
        long newTotalXp = currentXp + bonusXp;

        user.setTotalExperience(newTotalXp);
        userRepository.save(user);
        leaderboardService.updateScore(user.getId(), user.getUsername(), newTotalXp);

        return String.format("Systems synchronized! Current streak: %d days. %d XP earned.",
        user.getCurrentStreak(), bonusXp);
        }

        @Scheduled(cron = "0 0 0 * * *")
        @Transactional
        public void executeMidnightReset() {
            log.info( "[CHRONOS] Starting the nightly reset of broken streaks...");
            LocalDate yesterday = LocalDate.now().minusDays(1);

            userRepository.resetBrokenStreaks(yesterday);
            log.info("[CHRONOS] Database synchronized. Inactive citizen streaks reset.\")");
        }

    }
