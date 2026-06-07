package com.acrobtw.elei.domain.quest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acrobtw.elei.domain.quest.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByIsDefaultTrue();
}
