package com.acrobtw.elei.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acrobtw.elei.entity.DiaryEntry;


@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
Optional<DiaryEntry> findByTitle(String title);
}
