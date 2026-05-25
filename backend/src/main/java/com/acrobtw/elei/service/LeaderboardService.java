package com.acrobtw.elei.service;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.dto.leaderboard.LeaderboardEntryDto;
import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private static final String LEADERBOARD_KEY = "elei:leaderboard";

    public void updateScore(Long userId, Long newTotalXp) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, userId.toString(), newTotalXp.doubleValue());
    }

    public Set<LeaderboardEntryDto> getTopPlayers(int limit) {
        Set<ZSetOperations.TypedTuple<String>> topRedis =
            redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, limit - 1);

        if (topRedis == null || topRedis.isEmpty()) {
            return Collections.emptySet();
        }

        List<Long> userIds = topRedis.stream()
        .map(tuple -> Long.parseLong(tuple.getValue()))
        .toList();

        List<User> users = userRepository.findAllById(userIds);
        Map<Long, String> usernameMap = users.stream()
        .collect(Collectors.toMap(User::getId, User::getUsername));

        return topRedis.stream()
        .map(tuple -> {
            Long id = Long.parseLong(tuple.getValue());
            String name = usernameMap.getOrDefault(id, "Unknown Citizen");
            Long score = tuple.getScore() != null ? tuple.getScore().longValue() : 0L;

            return new LeaderboardEntryDto(id, name, score);
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Long getUserRank(Long userId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, userId.toString());
        return rank != null ? rank + 1 : null;
    }
}
