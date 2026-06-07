package com.acrobtw.elei.domain.quest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.repository.QuestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class QuestSeeder {

    private final QuestRepository questRepository;

    @Bean
    public CommandLineRunner initializeCoreQuests() {
        return args -> {
            if (questRepository.count() == 0) {
                log.info("[SYSTEM] Empty quest database detected. Initializing core protocols...");

                Quest awakening = Quest.builder()
                        .title("Awakening")
                        .description("Initiate the system sync by activating the daily check-in protocol.")
                        .targetCount(1)
                        .rewardXp(100)
                        .eventType(EventType.CHECK_IN)
                        .build();

                Quest firstBlood = Quest.builder()
                        .title("First Blood")
                        .description("Log your first completed activity into the system.")
                        .targetCount(1)
                        .rewardXp(150)
                        .eventType(EventType.ACTIVITY_COMPLETED)
                        .build();

                Quest consistency = Quest.builder()
                        .title("Consistency")
                        .description("Maintain an active protocol streak for 3 consecutive days.")
                        .targetCount(3)
                        .rewardXp(300)
                        .eventType(EventType.STREAK_MAINTAINED)
                        .build();

                questRepository.saveAll(List.of(awakening, firstBlood, consistency));
                log.info("[SYSTEM] Core quest protocols successfully injected.");
            }
        };
    }
}
