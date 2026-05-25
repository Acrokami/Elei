package com.acrobtw.elei.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.acrobtw.elei.dto.leaderboard.LeaderboardEntryDto;
import com.acrobtw.elei.service.LeaderboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/top")
    public ResponseEntity<Set<LeaderboardEntryDto>> getTopDashboard(@RequestParam(defaultValue = "10")int limit) {
        return ResponseEntity.ok(leaderboardService.getTopPlayers(limit));
    }
}
