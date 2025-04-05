package com.klp.oppgave.user;

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
        return userRepository.save(user);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByType(String filter) {
        List<User> allUsers = userRepository.findAll();
        if (filter == null) {
            return allUsers;
        } else {
            return allUsers.stream()
                    .filter(user -> user.getType().equals(filter.toUpperCase()))
                    .toList();
        }
    }
}
