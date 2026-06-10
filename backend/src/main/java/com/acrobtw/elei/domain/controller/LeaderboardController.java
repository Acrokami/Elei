package com.acrobtw.elei.domain.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.acrobtw.elei.domain.leaderboard.LeaderboardService;
import com.acrobtw.elei.domain.leaderboard.dto.LeaderboardEntryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
@Tag(name = "Global rating", description = "Hall of Fame Management (Redis ZSET)")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Operation(
        summary = "Get the leaderboard",
        description = "Returns a sorted array of the top citizens of Elei based on their experience."
    )
    @GetMapping("/top")
    public ResponseEntity<Set<LeaderboardEntryDto>> getTopDashboard(@RequestParam(defaultValue = "10")int limit) {
        return ResponseEntity.ok(leaderboardService.getTopPlayers(limit));
    }
}
