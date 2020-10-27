package com.zak.hoaxify;


import com.zak.hoaxify.Error.ApiError;
import com.zak.hoaxify.User.User;
import com.zak.hoaxify.User.UserRepository;
import com.zak.hoaxify.shared.GenericResponse;
import org.apache.tomcat.util.http.parser.AcceptLanguage;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public <T>ResponseEntity<T> postSignUp(Object request , Class <T> response , String langue){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", langue);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
        return testRestTemplate.postForEntity(API_1_0_USERS,httpEntity,response);
    }

    @Test
    public void postUser_WhenUserIsValid_receivedOk(){
        User user = TestUtil.createValidUser();
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_WhenUserIsValid_userSavedToDataBase(){
        User user = TestUtil.createValidUser();
        postSignUp( user, Object.class , "en");
        Assertions.assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_WhenUserIsValid_receivedSuccessMessage(){
        User user = TestUtil.createValidUser();
        ResponseEntity<GenericResponse> response = postSignUp( user, GenericResponse.class , "en");
        Assertions.assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_WhenUserIsValid_passwordIsHashedInDB(){
        User user = TestUtil.createValidUser();
        postSignUp( user, Object.class , "en");
        User inDb = userRepository.findAll().get(0);
        Assertions.assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasNullUsername_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setUsername(null);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasNullDisplayName_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setDisplayName(null);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasNullPassword_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasLessThanRequiredUsername_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setUsername("abc");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasLessThanRequiredDisplayName_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setDisplayName("abc");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasLessThanRequiredPassword_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("P4ssw");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasExceedUsernameLength_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        String stringOf300Chars = IntStream.rangeClosed(1,300).mapToObj(x -> "a").collect(Collectors.joining());
        user.setUsername(stringOf300Chars);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasExceedDisplayNameLength_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        String stringOf300Chars = IntStream.rangeClosed(1,300).mapToObj(x -> "a").collect(Collectors.joining());
        user.setDisplayName(stringOf300Chars);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasExceedPasswordLength_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        String stringOf300Chars = IntStream.rangeClosed(1,300).mapToObj(x -> "a").collect(Collectors.joining());
        user.setPassword(stringOf300Chars);
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasPasswordWithAllLowerCase_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("alllowercase");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasPasswordWithAllUpperCase_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("ALLUPPERCASE");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenUserHasPasswordWithOnlyNumbers_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("123456789");
        ResponseEntity<Object> response = postSignUp( user, Object.class , "en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserIsInvalid__receiveApiError(){
        User user = new User();
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        Assertions.assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
    }
    @Test
    public void postUser_whenUserIsInvalid__receiveApiErrorWithValidationErrors(){
        User user = new User();
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        Assertions.assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
    }
    @Test
    public void postUser_whenUserHasNullUsername__receiveGenericNullMsg(){
        User user = TestUtil.createValidUser();
        user.setUsername(null);
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        String errorMsg = response.getBody().getValidationErrors().get("username");
        Assertions.assertThat(errorMsg).isEqualTo("must not be null");
    }
    @Test
    public void postUser_whenUserHasNullPassword__receiveGenericNullMsg(){
        User user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class , "en");
        String errorMsg = response.getBody().getValidationErrors().get("password");
        Assertions.assertThat(errorMsg).isEqualTo("must not be null");
    }
    @Test
    public void postUser_whenUserHasNullDisplayName__receiveGenericNullMsg(){
        User user = TestUtil.createValidUser();
        user.setDisplayName(null);
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class , "en");
        String errorMsg = response.getBody().getValidationErrors().get("displayName");
        Assertions.assertThat(errorMsg).isEqualTo("must not be null");
    }
    @Test
    public void postUser_whenUserHasInvalidUsernameLength__receiveGenericSizelMsg(){
        User user = TestUtil.createValidUser();
        user.setUsername("abc");
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class , "en");
        String errorMsg = response.getBody().getValidationErrors().get("username");
        Assertions.assertThat(errorMsg).isEqualTo("size must be between 4 and 255");
    }
    @Test
    public void postUser_whenUserHasExceedPasswordLength__receiveGenericSizelMsg(){
        User user = TestUtil.createValidUser();
        user.setPassword("P4w");
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class , "en");
        String errorMsg = response.getBody().getValidationErrors().get("password");
        Assertions.assertThat(errorMsg).isEqualTo("size must be between 8 and 255");
    }
    @Test
    public void postUser_whenUserHasInvalidDisplayNameLength__receiveGenericSizelMsg(){
        User user = TestUtil.createValidUser();
        user.setDisplayName("abc");
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class , "en");
        String errorMsg = response.getBody().getValidationErrors().get("displayName");
        Assertions.assertThat(errorMsg).isEqualTo("size must be between 4 and 255");
    }
    @Test
    public void postUser_whenUserHasInvalidPasswordPattern__receiveCustomPatternError(){
        User user = TestUtil.createValidUser();
        user.setPassword("password");
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        String errorMsg = response.getBody().getValidationErrors().get("password");
        Assertions.assertThat(errorMsg).isEqualTo("Password must have atleast one Uppercase and one number");
    }
    @Test
    public void postUser_whenAnotherUserHasSameUsername_receiveBadRequest(){
        userRepository.save(TestUtil.createValidUser());
        User user = TestUtil.createValidUser();
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void postUser_whenAnotherUserHasSameUsername_receiveDuplicationMessage(){
        userRepository.save(TestUtil.createValidUser());
        User user = TestUtil.createValidUser();
        ResponseEntity<ApiError> response = postSignUp( user, ApiError.class ,"en");
        Assertions.assertThat(response.getBody().getValidationErrors().get("username")).isEqualTo("this name is in use");
    }


}
