package com.acrobtw.elei.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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

@Column(name = "points")
private Integer points;

@Column(name = "amount")
private Integer amount;

@Column(name = "created_at", nullable = false, updatable = false)
@CreationTimestamp
private LocalDateTime createdAt;


@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "user_id", nullable = false)
private User user;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "activity_id", nullable = false)
private Activity activity;

public ExperienceLog(Activity activity, Integer amount) {
    this.activity = activity;
    this.amount = amount;
    this.points = amount * activity.getBaseExperience();
}
}
