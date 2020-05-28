package com.application.alledrogo.repository;

import com.application.alledrogo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    private User alex;

    @Test
    void findByEmail() {
        alex = new User();

        alex.setEmail("alex@gmail.com");

        userRepository.save(alex);

        Optional<User> found = userRepository.findByEmail(alex.getEmail());

        assertThat(found.get().getEmail()).isEqualTo(alex.getEmail());
    }
}