package com.application.alledrogo.service;

import com.application.alledrogo.exception.NotAcceptableException;
import com.application.alledrogo.exception.NotFoundException;
import com.application.alledrogo.model.User;
import com.application.alledrogo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> getUsers = userRepository.findAll();

        if(getUsers.isEmpty()) {
            throw new NotFoundException("Not found any Users");
        }

        return  getUsers;
    }

    public User getUserById(int id) {
        Optional<User> getUser = userRepository.findById(id);
        return getUser.orElseThrow(() -> new NotFoundException(String.format("Not found User with id %s", id)));
    }

    public User getUserByEmail(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        return getUser.orElseThrow(() -> new NotFoundException(String.format("Not found User with email: %s", email)));
    }

    public User addUser(User user) {
        if(user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
            throw new NotAcceptableException("FirstName, LastName and Email should not be empty");
        }

        //isEmailTaken check if in database exist email, if yes throw NotAcceptableException, if not return false
        isEmailTaken(user.getEmail());

        return userRepository.save(user);
    }

    public User updateUserById(User user) {
        //getUserById check if the user exist in the database, if not throw NotFoundException (404 not found)
        User getUser = getUserById(user.getId());

        if(user.getEmail() != null) {
            //isEmailTaken check if email exist in database, if yes throw NotAcceptableException, if not return false
            isEmailTaken(user.getEmail());
        }

        if(user.getFirstName() == null) {
            user.setFirstName(getUser.getFirstName());
        }

        if(user.getLastName() == null) {
            user.setLastName(getUser.getLastName());
        }

        if(user.getEmail() == null) {
            user.setEmail(getUser.getEmail());
        }

        if(user.getBirthDate() == null) {
            user.setBirthDate(getUser.getBirthDate());
        }

        if(user.getPhoneNumber() == null) {
            user.setPhoneNumber(getUser.getPhoneNumber());
        }

        if(user.getStreet() == null) {
            user.setStreet(getUser.getStreet());
        }

        if(user.getHouseNumber() == 0) {
            user.setHouseNumber(getUser.getHouseNumber());
        }

        if(user.getFlatNumber() == 0) {
            user.setFlatNumber(getUser.getFlatNumber());
        }

        if(user.getPostCode() == null) {
            user.setPostCode(getUser.getPostCode());
        }

        if(user.getPostOffice() == null) {
            user.setPostOffice(getUser.getPostOffice());
        }

        return userRepository.save(user);
    }

    public void deleteUserById(int id) {
        //getUserById check if the user exist in the database, if not throw NotFoundException (404 not found)
        User getUser = getUserById(id);
        userRepository.delete(getUser);
    }

    public boolean isEmailTaken(String email) {
        Optional<User> getUserByEmail = userRepository.findByEmail(email);

        if(getUserByEmail.isPresent()) {
            throw new NotAcceptableException(String.format("User with email: %s exist in database with id: %s2", getUserByEmail.get().getEmail(), getUserByEmail.get().getId()));
        }

        return false;
    }
}
