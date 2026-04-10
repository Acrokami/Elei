package com.acrobtw.elei.repository;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.acrobtw.elei.entity.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    void shoudFoundEmail() {

        User user = new User();
        user.setUsername("Test");
        user.setPassword("test");
        user.setEmail("test@example.com");

        Optional<User> check = repository.findByEmail("test@example.com");

        assertNotNull(check);
    }
}
