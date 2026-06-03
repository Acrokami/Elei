package com.acrobtw.elei.domain.quest;


import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.domain.quest.dto.QuestProgressDto;
import com.acrobtw.elei.domain.quest.service.QuestEngineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
@Tag(name = "Quests", description = "Quest and achievement tracking protocol")
public class QuestController {

    private final QuestEngineService questEngineService;

    @Operation(summary = "Get user quests", description = "Returns the list of quests and current progress for the authenticated citizen")
    @GetMapping
    public ResponseEntity<List<QuestProgressDto>> getUserQuests(Principal principal) {
        return ResponseEntity.ok(questEngineService.getUserQuests(principal.getName()));
    }

}
