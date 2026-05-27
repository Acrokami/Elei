package com.acrobtw.elei.domain.activity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
Optional<Activity> findById(Long id);
Optional<Activity> findByName(String name);

List<Activity> findByUserId(Long id);
}
