package com.application.alledrogo.controller;

import com.application.alledrogo.exception.NotAcceptableException;
import com.application.alledrogo.model.User;
import com.application.alledrogo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<User> getAllUsers() {
        return userService.getAllUsers();
   }

    @GetMapping(
            path = "{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User getUserById(@PathVariable("userId") int id) {
        return userService.getUserById(id);
    }

    @GetMapping(
            path = "email/{email}/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User addUser(@RequestBody User user, Errors errors) {
        if(errors.hasErrors()) {
            throw new NotAcceptableException(errors.getFieldError().getField()+" "+errors.getFieldError().getDefaultMessage());
        }

        return userService.addUser(user);
    }

    @PutMapping(
            path = "{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public User updateUserById(@PathVariable("userId") int id, @RequestBody User user, Errors errors) {
        if(errors.hasErrors()) {
            throw new NotAcceptableException(errors.getFieldError().getField()+" "+errors.getFieldError().getDefaultMessage());
        }

        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping(
            path = "{userId}"
    )
    public void deleteUserById(@PathVariable("userId") int id) {
        userService.deleteUserById(id);
    }
}
