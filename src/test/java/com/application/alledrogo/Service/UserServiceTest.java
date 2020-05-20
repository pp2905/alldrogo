package com.application.alledrogo.Service;

import com.application.alledrogo.Exception.NotAcceptableException;
import com.application.alledrogo.Exception.NotFoundException;
import com.application.alledrogo.Model.User;
import com.application.alledrogo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Alex");
        user.setLastName("Texas");
        user.setEmail("alex@mail.com");
    }

    @Test
    void shouldGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        given(userRepository.findAll()).willReturn(userList);

        List<User> foundUsers = userService.getAllUsers();

        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0)).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldThrowExceptionWhenNoUsersInDatabase() {
        List<User> userList = new ArrayList<>();

        given(userRepository.findAll()).willReturn(userList);

        assertThrows(NotFoundException.class, () -> {
            userService.getAllUsers();
        });
    }

    @Test
    void shouldGetUserById() {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User found = userService.getUserById(user.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldGetUserByEmail() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));

        User found = userService.getUserByEmail(user.getEmail());

        assertThat(found).isNotNull();
        assertThat(found.getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    void shouldAddUser() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        User addedUser = userService.addUser(user);

        assertThat(addedUser).isNotNull();
        assertThat(addedUser).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldThrowExceptionWhenSaveUserWithNoRequiredData() {
        User emptyUser = new User();

        given(userRepository.save(emptyUser)).willReturn(emptyUser);

        assertThrows(NotAcceptableException.class, () -> {
            userService.addUser(emptyUser);
        });
    }

    @Test
    void shouldUpdateUserById() {
    }

    @Test
    void shouldDeleteUserById() {

    }

    @Test
    void isEmailTaken() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(NotAcceptableException.class, () -> {
            userService.isEmailTaken(user.getEmail());
        });
    }
}