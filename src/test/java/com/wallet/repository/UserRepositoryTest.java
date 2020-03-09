package com.wallet.repository;

import com.wallet.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    public static final String EMAIL = "email@teste.com";
    static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setUp(){
    User u = new User();
    u.setName("SetupUser");
    u.setPassword("Senha 123");
    u.setEmail(EMAIL);
    repository.save(u);
    }

    @AfterEach
    public void tearDown(){
        repository.deleteAll();
    }

    @Test
    public void testSave(){
        User u = new User();
        u.setName("Test");
        u.setPassword("123456");
        u.setEmail("eles@teste.com");

        User response = repository.save(u);

        assertNotNull(response);
    }

    @Test
    public void testFindByEmail(){
        Optional<User> response = repository.findByEmailEquals(EMAIL);
        LOG.info(response.toString());
        assertTrue(response.isPresent());
        assertEquals(response.get().getEmail(),EMAIL);
    }

}
