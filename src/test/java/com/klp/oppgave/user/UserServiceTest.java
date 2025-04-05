package com.klp.oppgave.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByType() {
        User user1 = new User("user1@example.com", "USER");
        User user2 = new User("user2@example.com", "ADMIN");
        User user3 = new User("user3@example.com", "USER");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        List<User> resultUser = userService.getUsersByType("USER");
        List<User> resultAdmin = userService.getUsersByType("admin");
        List<User> resultNoParam = userService.getUsersByType(null);
        List<User> resultOtherParam = userService.getUsersByType("GUEST");

        assertEquals(2, resultUser.size());
        assertEquals(1, resultAdmin.size());
        assertEquals(3, resultNoParam.size());
        assertEquals(0, resultOtherParam.size());

        assertEquals("user1@example.com", resultUser.getFirst().getEmail());
    }
}
