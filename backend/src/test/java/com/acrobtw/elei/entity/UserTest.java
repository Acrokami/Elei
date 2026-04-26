package com.acrobtw.elei.entity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.junit.jupiter.api.Test;

import com.acrobtw.elei.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@SuppressWarnings("unused")
public class UserTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldPersistAndRetrieveUserByEmail() {
        User user = new User("Test", "test@gmail.com", "test");
        User savedUser = userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        User foundUser = userRepository.findByEmail("test@gmail.com").
        orElseThrow(() -> new IllegalArgumentException("This user is not exist"));

        assertNotNull(foundUser);
        assertNotNull(foundUser.getId(), "ID Should Be Generated");
        assertEquals("Test", foundUser.getUsername());
        assertEquals("test@gmail.com", foundUser.getEmail());
    }
}
