package com.acrobtw.elei.entity;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.acrobtw.elei.repository.DiaryEntryRepository;

@DataJpaTest
@SuppressWarnings("unused")
public class DiaryEntryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DiaryEntryRepository diaryRepository;


    @Test
    public void shouldSaveAndFindDiaryEntryByTitle(){

        User testUser = new User("Test", "test@gmail.com", "test");
        testUser = entityManager.persistAndFlush(testUser);

        DiaryEntry diary = new DiaryEntry("Day One", "Hello world");
        diary.setUser(testUser);

        DiaryEntry savedDiary = diaryRepository.save(diary);
        entityManager.flush();
        entityManager.clear();

        DiaryEntry foundDiary = diaryRepository.findByTitle("Day One")
        .orElseThrow(() -> new IllegalArgumentException("This diary doesnt exist"));

        assertNotNull(foundDiary);
        assertEquals("Hello world", foundDiary.getContent());
        assertNotNull(foundDiary.getUser());
        assertEquals(testUser.getId(), foundDiary.getUser().getId());
    }
}
