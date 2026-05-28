package com.acrobtw.elei.domain.user.experience;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExperienceLogRepository extends JpaRepository<ExperienceLog, Long> {

    @Query("SELECT l.activity.id, COALESCE(SUM(l.earnedXp), 0) FROM ExperienceLog l WHERE l.user.id = :userId GROUP BY l.activity.id")
    List<Object[]> sumAllPointsByUserGroupedByActivity(@Param("userId") Long userId);
    List<ExperienceLog> findTop30ByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByActivityId(Long activityId);
}
