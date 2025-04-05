package com.klp.oppgave.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testValidEmailAndType() {
        User user = new User("user@example.com", "USER");
        assertEquals("user@example.com", user.getEmail());
        assertEquals("USER", user.getType());
    }

    @Test
    void testInvalidEmailThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("invalidEmail", "USER"));
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void testInvalidTypeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new User("user@example.com", "GUEST"));
        assertEquals("Invalid user type: GUEST", exception.getMessage());
    }

    @Test
    void testSetEmailWithValidValue() {
        User user = new User("user@example.com", "USER");
        user.setEmail("newuser@example.com");
        assertEquals("newuser@example.com", user.getEmail());
    }

    @Test
    void testSetEmailWithInvalidValue() {
        User user = new User("user@example.com", "USER");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalidEmail"));
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void testSetTypeWithValidValue() {
        User user = new User("user@example.com", "USER");
        user.setType("ADMIN");
        assertEquals("ADMIN", user.getType());
    }

    @Test
    void testSetTypeWithInvalidValue() {
        User user = new User("user@example.com", "USER");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setType("INVALID_TYPE"));
        assertEquals("Invalid user type: INVALID_TYPE", exception.getMessage());
    }
}
