package com.zak.hoaxify;

import com.zak.hoaxify.User.User;
import com.zak.hoaxify.User.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
//To tell spring to inject just the requirement beans for JPa , so that the test can run faster
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void findByUsername_whenUserExist_reveiceUser(){
        User user = new User();
        user.setUsername("my-user");
        user.setDisplayName("my-displayName");
        user.setPassword("P4ssword");
        testEntityManager.persist(user);
        Optional<User> inDb = userRepository.findByUsername(user.getUsername());
        Assertions.assertThat(inDb.isPresent()).isTrue();
    }
    @Test
    public void findByUsername_whenUserDoesntExist_reveiceNull(){
        Optional<User> inDb = userRepository.findByUsername("my-user");
        Assertions.assertThat(inDb.isPresent()).isFalse();
    }
}
