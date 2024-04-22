package com.phildev.pcs.service;


import com.phildev.pcs.domain.User;
import com.phildev.pcs.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.UserController} and {@link UserRepository}
 * All the methods in this service helps saving, deleting, retrieving {@link User} in database
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * This method is searching a user by its id in database through the {@link UserRepository#findById(Object)} method
     * @param id which is the user id
     * @return an optional {@link User}
     */
    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }

    /**
     * This method calls {@link UserRepository#findAll()} method to find all users in database
     * @return a list of User
     */
    public List<User> findAll(){
        return userRepository.findAll();
    }

    /**
     * This method is deleting a user passed as a parameter through the method {@link UserRepository#delete(Object)}
     * @param user
     */
    public void delete(User user){
        userRepository.delete(user);
    }

    /**
     * This method checks whether a username or full name already exists in database
     * It uses spring data jpa methods {@link UserRepository#findByUsername(String)} and {@link UserRepository#findByUsername(String)}
     * @param user
     * @return true or false
     */
    public boolean checkUserNameOrFullNameExist(User user){
        return (userRepository.findByUsername(user.getUsername()) != null) || (userRepository.findByFullName(user.getFullName()) != null);
    }
}
