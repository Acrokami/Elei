package com.acrobtw.elei.domain.quest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.experience.ExperienceService;
import com.acrobtw.elei.domain.quest.Quest;
import com.acrobtw.elei.domain.quest.UserQuestProgress;
import com.acrobtw.elei.domain.quest.dto.QuestProgressDto;
import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.repository.QuestRepository;
import com.acrobtw.elei.domain.quest.repository.UserQuestProgressRepository;
import com.acrobtw.elei.domain.user.User;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestEngineService {

    private final UserQuestProgressRepository progressRepository;
    private final QuestRepository questRepository;
    private final ExperienceService experienceService;



    @Transactional
    public void assignInitialQuests(User user) {
        List<Quest> defaultQuests = questRepository.findAll();

        if(defaultQuests.isEmpty()) {
            log.warn("[SYSTEM] No core quests found in database: Initial assignment skipped");
            return;
        }

        List<UserQuestProgress> initialProgress = defaultQuests.stream()
        .map(quest -> {
            UserQuestProgress progress = new UserQuestProgress();
            progress.setUser(user);
            progress.setQuest(quest);
            progress.setCurrentCount(0);
            progress.setIsCompleted(false);
            return progress;
        })
        .toList();

        progressRepository.saveAll(initialProgress);
        log.info("[SYSTEM] Initialized {} core protocols for new citizen: {}", initialProgress.size(), user.getUsername());
    }


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


    @Transactional(readOnly = true)
    public List<QuestProgressDto> getUserQuests(String username) {
        return progressRepository.findByUserUsername(username).stream()
            .map(progress -> new QuestProgressDto(
                progress.getQuest().getId(),
                progress.getQuest().getTitle(),
                progress.getQuest().getDescription(),
                progress.getCurrentCount(),
                progress.getQuest().getTargetCount(),
                progress.getQuest().getRewardXp(),
                progress.getIsCompleted()
            ))
            .toList();
    }

}
