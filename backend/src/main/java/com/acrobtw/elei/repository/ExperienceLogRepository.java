package com.acrobtw.elei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acrobtw.elei.entity.ExperienceLog;

public interface ExperienceLogRepository extends JpaRepository<ExperienceLog, Long> {

    @Query("SELECT COALESCE(SUM(l.earnedXp), 0) FROM ExperienceLog l WHERE l.user.id = :userId AND l.activity.id = :activityId")
    Integer sumPointsByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);


    List<ExperienceLog> findTop30ByUserIdOrderByCreatedAtDesc(Long userId);
}
