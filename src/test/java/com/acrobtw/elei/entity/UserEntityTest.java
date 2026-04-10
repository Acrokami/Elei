package com.acrobtw.elei.entity;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;




@DataJpaTest
public class UserEntityTest {


    @Autowired
    private TestEntityManager entityManager;


    @Test
    void shouldSaveAndRetrieveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setUsername("Test");

        User saved = entityManager.persistAndFlush(user);
        User found = entityManager.find(User.class, saved.getId());

        assertNotNull(found);
        assertNotNull(found.getId());
        assertEquals("test@example.com", found.getEmail());
    }

}
