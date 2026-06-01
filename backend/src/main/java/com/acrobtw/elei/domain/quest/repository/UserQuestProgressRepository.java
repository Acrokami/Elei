package com.acrobtw.elei.domain.quest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acrobtw.elei.domain.quest.UserQuestProgress;
import com.acrobtw.elei.domain.quest.enums.EventType;

public interface UserQuestProgressRepository extends JpaRepository<UserQuestProgress, Long> {

    @Query("SELECT p FROM UserQuestProgress p JOIN FETCH p.quest q " +
           "WHERE p.user.username = :username " +
           "AND q.eventType = :eventType " +
           "AND p.isCompleted = false")
    List<UserQuestProgress> findActiveQuestsByUserAndEventType(
        @Param("username") String username,
        @Param("eventType") EventType eventType
    );
}
