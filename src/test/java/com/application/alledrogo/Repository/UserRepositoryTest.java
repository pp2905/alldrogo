package com.application.alledrogo.Repository;

import com.application.alledrogo.Model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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