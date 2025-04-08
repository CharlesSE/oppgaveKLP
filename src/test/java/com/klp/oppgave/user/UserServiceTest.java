package com.klp.oppgave.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateUserValid() {
        User validUser = new User("user@example.com", "USER");
        when(userRepository.save(validUser)).thenReturn(validUser);

        User createdUser = userService.createUser(validUser);
        assertEquals("user@example.com", createdUser.getEmail());
        assertEquals("USER", createdUser.getType());
    }

    @Test
    void testGetUserByIdFound() {
        User user = new User("user@example.com", "USER");
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.getUserById(1);
        assertEquals(1, foundUser.getId());
        assertEquals("user@example.com", foundUser.getEmail());
        assertEquals("USER", foundUser.getType());
    }

    @Test
    void testGetUserByIdNotFoundThrowsException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(999));
        assertEquals("User with ID 999 not found", exception.getMessage());
    }

    @Test
    void testGetUsersByTypeWithFilter() {
        User user1 = new User("user1@example.com", "USER");
        User user2 = new User("admin@example.com", "ADMIN");
        when(userRepository.findByType("USER")).thenReturn(List.of(user1));

        List<User> users = userService.getUsersByType("USER");
        assertEquals(1, users.size());
        assertEquals("user1@example.com", users.getFirst().getEmail());
    }

    @Test
    void testGetUsersByTypeWithoutFilter() {
        User user1 = new User("user1@example.com", "USER");
        User user2 = new User("admin@example.com", "ADMIN");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getUsersByType(null);
        assertEquals(2, users.size());
    }
}
