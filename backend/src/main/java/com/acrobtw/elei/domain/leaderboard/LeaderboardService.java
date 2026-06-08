package com.acrobtw.elei.domain.leaderboard;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.leaderboard.dto.LeaderboardEntryDto;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private static final String LEADERBOARD_KEY = "elei:leaderboard";

    public void updateScore(Long userId, String username, Long newTotalXp) {
        String member = userId + ":" + username;
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, member, newTotalXp.doubleValue());
    }


    public Set<LeaderboardEntryDto> getTopPlayers(int limit) {
        Set<ZSetOperations.TypedTuple<String>> topRedis =
            redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, limit - 1);

        if (topRedis == null || topRedis.isEmpty()) {
            return Collections.emptySet();
        }

        return topRedis.stream()
        .filter(tuple -> tuple.getValue() != null)
        .map(tuple -> {
            String member = tuple.getValue();
            String[] parts = member.split(":", 2);

            Long id = 0L;
            String name = "Unknown citizen";
            if(parts.length == 2) {
                try {
                    id = Long.parseLong(parts[0]);
                    name = parts[1];
                } catch (NumberFormatException ignored) {}
            } else {
                try {
                    id = Long.parseLong(parts[0]);
                } catch (NumberFormatException ignored) {}
            }
            Long score = tuple.getScore() != null ? tuple.getScore().longValue() : 0L;

            return new LeaderboardEntryDto(id, name, score);
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Optional<Long> getUserRank(Long userId, String username) {
        String member = userId + ":" + username;
        Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, member);
        return Optional.ofNullable(rank).map(r -> r + 1);
    }



    @EventListener(ApplicationReadyEvent.class)
    public void warmUpLeaderboardCache() {
        List<User> allUsers = userRepository.findAll();

        if(allUsers.isEmpty()) {
            log.info("[SYSTEM] Database is empty. Leaderboard warm up cancelled");
            return;
        }

        redisTemplate.delete(LEADERBOARD_KEY);

        Set<ZSetOperations.TypedTuple<String>> tuples = allUsers.stream()
                .map(user -> {
                    double score = user.getTotalExperience() != null
                    ? user.getTotalExperience().doubleValue()
                    : 0.0;

                    String member = user.getId() + ":" + user.getUsername();
                    return new DefaultTypedTuple<>(member, score);
                })
                .collect(Collectors.toSet());

        if (!tuples.isEmpty()) {
            String tempKey = LEADERBOARD_KEY + ":tmp";
            redisTemplate.opsForZSet().add(tempKey, tuples);
            redisTemplate.rename(tempKey, LEADERBOARD_KEY);
            log.info("[SYSTEM] Warm up completed. Sync is ready");
        }
    }
}
