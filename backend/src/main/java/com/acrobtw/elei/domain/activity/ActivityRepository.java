package com.acrobtw.elei.domain.activity;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;




public interface ActivityRepository extends JpaRepository<Activity, Long> {

Optional<Activity> findByName(String name);

}
