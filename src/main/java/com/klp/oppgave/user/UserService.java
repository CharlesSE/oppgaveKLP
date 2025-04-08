package com.klp.oppgave.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getType() == null) {
            throw new IllegalArgumentException("Email and type are required");
        }
        return userRepository.save(user);
    }


    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    public List<User> getUsersByType(String filter) {
        if (filter == null) {
            return userRepository.findAll();
        } else {
            return userRepository.findByType(filter.toUpperCase());
        }
    }
}
