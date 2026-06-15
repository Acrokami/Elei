package com.acrobtw.elei.domain.activity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import com.acrobtw.elei.domain.experience.ExperienceLog;


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

    @Column(name = "measurement_unit", nullable = false)
    private String measurementUnit;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperienceLog> experienceLogs = new ArrayList<>();

    public Activity(String name, Integer pointsMultiplier, String measurementUnit) {
        this.name = name;
        this.pointsMultiplier = pointsMultiplier;
        this.measurementUnit = measurementUnit;
    }

    public void addExperienceLog(ExperienceLog log) {
        experienceLogs.add(log);
        log.setActivity(this);
    }
}
