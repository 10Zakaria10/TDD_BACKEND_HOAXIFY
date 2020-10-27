package com.zak.hoaxify;

import com.zak.hoaxify.Error.ApiError;
import com.zak.hoaxify.User.User;
import com.zak.hoaxify.User.UserRepository;
import com.zak.hoaxify.User.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {

    private static final String API_1_0_LOGIN = "/api/1.0/login";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Before
    public void cleanUp() {
        userRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }


    @Test
    public void postLogin_withoutUserCredentials_receiveUnauthorized(){
        ResponseEntity<Object> response = login(Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_WithIncorrectCredentials_receiveUnauthorized(){
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_WithIncorrectCredentials_receiveApiError(){
        authenticate();
        ResponseEntity<ApiError> response = login(ApiError.class);
        Assertions.assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
    }

    @Test
    public void postLogin_WithIncorrectCredentials_receiveApiErrorWithoutValidationErrors(){
        authenticate();
        ResponseEntity<String> response = login(String.class);
        Assertions.assertThat(response.getBody().contains("validationErrors")).isFalse();
    }
    @Test
    public void postLogin_WithIncorrectCredentials_receiveUnauthorizedWithoutWWWAuthenticationHeader(){
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        Assertions.assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
    }
    @Test
    public void postLogin_WithValidCredentials_receiveOk(){
        User user = TestUtil.createValidUser();

        userService.save(user);
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private void authenticate() {
        testRestTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("test-user","P4ssword"));
    }

    public <T> ResponseEntity<T> login (Class<T> responseType )
    {
        return testRestTemplate.postForEntity(API_1_0_LOGIN,null,responseType);
    }
}
