package com.acrobtw.elei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acrobtw.elei.entity.ExperienceLog;

public interface ExperienceLogRepository extends JpaRepository<ExperienceLog, Long> {

    @Query("SELECT COALESCE(SUM(l.points), 0) FROM ExperienceLog l WHERE l.user.id = :userId AND l.activity_id = :activityId")
    Integer sumPointsByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);
}
