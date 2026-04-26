package com.acrobtw.elei.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acrobtw.elei.entity.Activity;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
Optional<Activity> findById(Long id);
Optional<Activity> findByName(String name);
}
