package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class Login {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @LocalServerPort
    private int port;

    public UserResponse login(String username, String password) throws URISyntaxException, HttpStatusCodeException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI("http://localhost:" + port +"/merchant/user/login");
        User user = User.builder().email(username).password(password).build();


        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<User> httpRequest = new HttpEntity<>(user, headers);
        UserResponse body = restTemplate.postForEntity(uri, httpRequest, UserResponse.class).getBody();
        return body;
    }
}
