package com.application.alledrogo.service;

import com.application.alledrogo.exception.NotAcceptableException;
import com.application.alledrogo.exception.NotFoundException;
import com.application.alledrogo.model.User;
import com.application.alledrogo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        user.setId(1);
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
        int id = user.getId();
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User found = userService.getUserById(id);

        assertThat(found).isNotNull();
        assertThat(found).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundUserWithId() {
        int id = user.getId();
        User nullUser = null;

        given(userRepository.findById(id)).willReturn(Optional.ofNullable(nullUser));

        assertThrows(NotFoundException.class, () -> {
            userService.getUserById(id);
        });
    }

    @Test
    void shouldGetUserByEmail() {
        String email = user.getEmail();
        given(userRepository.findByEmail(email)).willReturn(Optional.ofNullable(user));

        User found = userService.getUserByEmail(email);

        assertThat(found).isNotNull();
        assertThat(found.getEmail())
                .isEqualTo(email);

        verify(userRepository).findByEmail(email);
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

        assertThrows(NotAcceptableException.class, () -> {
            userService.addUser(emptyUser);
        });
    }

    @Test
    void shouldUpdateUserById() {
        int id = user.getId();

        given(userRepository.findById(id)).willReturn(Optional.ofNullable(user));
        given(userRepository.save(user)).willReturn(user);

        user.setFirstName("Testowy");
        User expected = userService.updateUserById(user);

        assertThat(expected).isNotNull();
        assertThat(expected).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldDeleteUserById() {
        int id = user.getId();

        given(userRepository.findById(id)).willReturn(Optional.ofNullable(user));
        userService.deleteUserById(id);

        verify(userRepository).delete(user);
    }

    @Test
    void isEmailTaken() {
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(NotAcceptableException.class, () -> {
            userService.isEmailTaken(user.getEmail());
        });
    }
}