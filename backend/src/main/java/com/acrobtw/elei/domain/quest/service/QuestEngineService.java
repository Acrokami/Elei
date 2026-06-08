package com.acrobtw.elei.domain.quest.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.core.exception.ResourceNotFoundException;
import com.acrobtw.elei.domain.experience.ExperienceService;
import com.acrobtw.elei.domain.quest.Quest;
import com.acrobtw.elei.domain.quest.UserQuestProgress;
import com.acrobtw.elei.domain.quest.dto.QuestProgressDto;
import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.repository.QuestRepository;
import com.acrobtw.elei.domain.quest.repository.UserQuestProgressRepository;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestEngineService {

    private final UserRepository userRepository;
    private final UserQuestProgressRepository progressRepository;
    private final QuestRepository questRepository;
    private final ExperienceService experienceService;
    private final SimpMessagingTemplate messagingTemplate;



    @Transactional
    public void assignInitialQuests(User user) {
        List<Quest> defaultQuests = questRepository.findByIsDefaultTrue();

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
        for(UserQuestProgress progress : activeProgress) {
            QuestProgressDto updatedQuest = new QuestProgressDto(
                progress.getQuest().getId(),
                progress.getQuest().getTitle(),
                progress.getQuest().getDescription(),
                progress.getCurrentCount(),
                progress.getQuest().getTargetCount(),
                progress.getQuest().getRewardXp(),
                progress.getIsCompleted()
            );
            messagingTemplate.convertAndSend("/topic/quests" + username, updatedQuest);
        }
    }


    @Transactional()
    public List<QuestProgressDto> getUserQuests(String username) {
        List<UserQuestProgress> progressList = progressRepository.findByUserUsername(username);

        if(progressList.isEmpty()) {
            log.info("[SYSTEM] Legacy citizen detected without protocols: {}. Initiating retroactive assignment...", username);

            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
            assignInitialQuests(user);
            progressList = progressRepository.findByUserUsername(username);
        }

        return progressList.stream()
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
