package com.example.demo.repository;

import com.example.demo.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("integrationtest")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ApiUserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find ApiUser by Username")
    public void whenFindByNameThenReturnApiUser() {
        User user = User.builder().email("demo@financialhouse.io").password(passwordEncoder.encode("cjaiU8CV")).build();
        Optional<User> foundUser = userRepository.findOneByEmail("demo@financialhouse.io");


        assertEquals(user.getEmail(),
                foundUser.get().getEmail(),
                "Problem with getting user");
    }
}
