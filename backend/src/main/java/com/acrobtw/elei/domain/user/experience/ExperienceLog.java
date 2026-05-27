package com.acrobtw.elei.domain.user.experience;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.acrobtw.elei.domain.activity.Activity;
import com.acrobtw.elei.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "experience_logs")
public class ExperienceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long id;

    @Column(name = "unitsCompleted")
    private Integer unitsCompleted;

    @Column(name = "earnedXp")
    private Integer earnedXp;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    public ExperienceLog(Activity activity, Integer unitsCompleted) {
        this.activity = activity;
        this.unitsCompleted = unitsCompleted;
        this.earnedXp = unitsCompleted * activity.getPointsMultiplier();
    }
}
