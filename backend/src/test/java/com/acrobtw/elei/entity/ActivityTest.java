package com.acrobtw.elei.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.acrobtw.elei.domain.activity.Activity;
import com.acrobtw.elei.domain.activity.ActivityRepository;
import com.acrobtw.elei.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@SuppressWarnings("unused")
public class ActivityTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldPersistAndRetrieveActivityByName() {
        User user = new User("Test", "test@gmail.com", "test");
        Activity sport = new Activity(user, "Sports", 0, "minutes");
        Activity savedActivity = activityRepository.save(sport);

        entityManager.flush();
        entityManager.clear();

        Activity foundSport = activityRepository.findByName("Sports").orElseThrow(() -> new IllegalArgumentException("This sport is not exist"));

        assertNotNull(foundSport);
        assertNotNull(foundSport.getId(), "ID Should Be Generated");
        assertEquals("Sports", sport.getName());
    }

}
