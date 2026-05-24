package com.acrobtw.elei.service;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;
    private static final String LEADERBOARD_KEY = "elei:leaderboard";

    public void updateScore(Long userId, Long newTotalXp) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, userId.toString(), newTotalXp.doubleValue());
    }

    public Set<ZSetOperations.TypedTuple<String>> getTopPlayers(int limit) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, limit - 1);
    }

    public Long getUserRank(Long userId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, userId.toString());
        return rank != null ? rank + 1 : null;
    }
}
