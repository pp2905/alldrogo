package com.application.alledrogo.controller;

import com.application.alledrogo.model.User;
import com.application.alledrogo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("alex");
        user.setLastName("Texas");
        user.setEmail("alex@mail.com");
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<User> allUsers = Arrays.asList(user);

        given(userService.getAllUsers()).willReturn(allUsers);

        mvc.perform(get("/api/users")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(user.getFirstName())));
    }

    @Test
    void shouldGetUserById() throws Exception {
        given(userService.getUserById(user.getId())).willReturn(user);

        mvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    void shouldGetUserByEmail() throws Exception {
        given(userService.getUserByEmail(user.getEmail())).willReturn(user);

        mvc.perform(get("/api/users/email/{email}/", user.getEmail())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    void shouldAddUser() throws Exception {
        given(userService.addUser(user)).willReturn(user);

        mvc.perform(post("/api/users/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"firstName\": \"alex\", \"lastName\": \"Texas\", \"email\": \"alex@mail.com\" }")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    void shouldUpdateUserById() throws Exception {
        given(userService.updateUser(user)).willReturn(user);

        mvc.perform(put("/api/users/{userId}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"firstName\": \"alex\", \"lastName\": \"Texas\", \"email\": \"alex@mail.com\" }")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        mvc.perform(delete("/api/users/{userId}", user.getId()))
                .andExpect(status().isOk());
    }
}