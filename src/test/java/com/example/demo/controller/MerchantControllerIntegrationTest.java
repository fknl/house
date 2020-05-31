package com.example.demo.controller;

import com.example.demo.config.AuthenticationErrorResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserResponse;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Merchant Controller Integration Tests")
@Execution(ExecutionMode.CONCURRENT)
public class MerchantControllerIntegrationTest extends Login{
    @Value("${jwt.expiration}")
    private long expiration;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Test
    @DisplayName("Login Failed with invalid password")
    public void testLoginFailedDueToInvalidPassword() throws URISyntaxException, IOException {
        try {

            login("demo@financialhouse.io", "xxxxxx");
        } catch (HttpStatusCodeException e) {
            AuthenticationErrorResponse aer = objectMapper.readValue(e.getResponseBodyAsString(), AuthenticationErrorResponse.class);
            assertAll(
                    () -> assertThat(e.getRawStatusCode(), is(equalTo(500))),
                    () -> assertThat(aer.getCode().intValue(), is(equalTo(0))),
                    () -> assertThat(aer.getStatus(), is(equalTo(Status.DECLINED))),
                    () -> assertThat(aer.getMessage(), is(equalTo("Error: Merchant User credentials is not valid")))
            );
        }
    }

    @Test
    @DisplayName("Login Failed with non exists User")
    public void testLoginFailedDueToNonExistingUser() throws URISyntaxException, IOException {
        try {
            login("ufukunlu@gmail.com", "cjaiU8CV");
        } catch (HttpStatusCodeException e) {
            AuthenticationErrorResponse aer = objectMapper.readValue(e.getResponseBodyAsString(), AuthenticationErrorResponse.class);
            assertAll(
                    () -> assertThat(e.getRawStatusCode(), is(equalTo(500))),
                    () -> assertThat(aer.getCode().intValue(), is(equalTo(0))),
                    () -> assertThat(aer.getStatus(), is(equalTo(Status.DECLINED))),
                    () -> assertThat(aer.getMessage(), is(equalTo("Error: Merchant User credentials is not valid")))
            );
        }
    }

    @Test
    @DisplayName("Login Success")
    public void testLoginSuccess() throws URISyntaxException {
       // userRepository.deleteAll();
        //userRepository.save(User.builder().email("demo@financialhouse.io").password(passwordEncoder().encode("cjaiU8CV")).build());

        UserResponse response = login("demo@financialhouse.io", "cjaiU8CV");
        assertAll(
                () -> assertThat(response.getToken(), not(emptyOrNullString())),
                () -> assertThat(response.getStatus(), is(equalTo(Status.APPROVED)))
        );
    }


}
