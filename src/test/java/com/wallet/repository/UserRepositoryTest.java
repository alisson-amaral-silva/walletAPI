package com.wallet.repository;

import com.wallet.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    public static final String EMAIL = "email@teste.com";
    static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);


    @Autowired
    UserRepository repository;

    @Before
    public void setUp(){
    User u = new User();
    u.setName("SetupUser");
    u.setPassword("Senha 123");
    u.setEmail(EMAIL);
    repository.save(u);

    }

    @After
    public void tearDown(){
        repository.deleteAll();
    }

    @Test
    public void testSave(){
        User u = new User();
        u.setName("Test");
        u.setPassword("123456");
        u.setEmail(EMAIL);

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
