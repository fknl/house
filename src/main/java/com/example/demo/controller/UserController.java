package com.example.demo.controller;

import com.example.demo.config.TokenUtil;
import com.example.demo.model.User;
import com.example.demo.model.UserResponse;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private TokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/merchant/user/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        User user = userRepository.findOneByEmail(authenticationRequest.getEmail()).map((u) -> new User(u.getId(), u.getEmail(), u.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authenticationRequest.getEmail()));
        final String token = jwtTokenUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>()));
        return ResponseEntity.ok(new UserResponse(token, Status.APPROVED));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}