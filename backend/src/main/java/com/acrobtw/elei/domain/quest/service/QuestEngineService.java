package com.acrobtw.elei.domain.quest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.experience.ExperienceService;
import com.acrobtw.elei.domain.quest.Quest;
import com.acrobtw.elei.domain.quest.UserQuestProgress;
import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.repository.UserQuestProgressRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestEngineService {

    private final UserQuestProgressRepository progressRepository;
    private final ExperienceService experienceService;


    @Transactional
    public void processEvent(String username, EventType eventType) {
        List<UserQuestProgress> activeProgress = progressRepository.
        findActiveQuestsByUserAndEventType(username, eventType);

        if(activeProgress.isEmpty()) {
            return;
        }

        for(UserQuestProgress progress : activeProgress) {
            Quest quest = progress.getQuest();
            progress.setCurrentCount(progress.getCurrentCount() + 1);

            log.debug("Citizen {} progressed in quest '{}': {}/{}",
                username, quest.getTitle(), progress.getCurrentCount(), quest.getTargetCount());

        if(progress.getCurrentCount() >= quest.getTargetCount()) {
            progress.setIsCompleted(true);
            experienceService.addRawExperience(username, quest.getRewardXp().longValue());
            log.info("Citizen {} completed quest: {}! Awarded {} XP.",
                    username, quest.getTitle(), quest.getRewardXp());

            // TODO Add NotificationDispatchService for Notification Toast from frontend
            }
        }

        progressRepository.saveAll(activeProgress);
    }

}
