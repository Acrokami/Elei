package com.acrobtw.elei.domain.activity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import com.acrobtw.elei.domain.experience.ExperienceLog;
import com.acrobtw.elei.domain.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "points_multiplier", nullable = false)
    private Integer pointsMultiplier;

    // TODO
    // New field for storing words: "minute", "page", "time", I understand that the field looks misleading and it is not immediately clear what it is, then I will do a rename or refactor
    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperienceLog> experienceLogs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Activity(User user, String name, Integer pointsMultiplier, String unitName) {
        this.user = user;
        this.name = name;
        this.pointsMultiplier = pointsMultiplier;
        this.unitName = unitName;
    }

    public void addExperienceLog(ExperienceLog log) {
        experienceLogs.add(log);
        log.setActivity(this);
    }
}
