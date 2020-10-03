package com.zak.hoaxify;


import com.zak.hoaxify.User.User;
import com.zak.hoaxify.User.UserRepository;
import com.zak.hoaxify.shared.GenericResponse;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    private static final String API_1_0_USERS = "/api/1.0/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    public void postUser_WhenUserIsValid_receivedOk(){
        User user = createValidUser();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_WhenUserIsValid_userSavedToDataBase(){
        User user = createValidUser();
        testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
        Assertions.assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_WhenUserIsValid_receivedSuccessMessage(){
        User user = createValidUser();
        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
        Assertions.assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_WhenUserIsValid_passwordIsHashedInDB(){
        User user = createValidUser();
        testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
        User inDb = userRepository.findAll().get(0);
        Assertions.assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
    }

    private User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-dispaly");
        user.setPassword("P4ssword");
        return user;
    }
}
