package com.klp.oppgave.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator;

    public UserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testValidEmailAndType() {
        User user = new User("user@example.com", "USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Valid User should pass validation.");
    }

    @Test
    void testInvalidEmail() {
        User user = new User("invalidEmail", "USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid email should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email must be a valid email address", violation.getMessage());
    }

    @Test
    void testNullEmail() {
        User user = new User(null, "USER");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Null email should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email cannot be null", violation.getMessage());
    }

    @Test
    void testInvalidType() {
        User user = new User("user@example.com", "GUEST");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid type should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Type must be either 'USER' or 'ADMIN'", violation.getMessage());
    }

    @Test
    void testNullType() {
        User user = new User("user@example.com", null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Null type should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Type cannot be null", violation.getMessage());
    }

    @Test
    void testValidSetters() {
        User user = new User("user@example.com", "USER");
        user.setEmail("newuser@example.com");
        user.setType("ADMIN");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Updated User with valid email and type should pass validation.");
    }

    @Test
    void testInvalidSetterEmail() {
        User user = new User("user@example.com", "USER");
        user.setEmail("invalidEmail");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid setter email should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email must be a valid email address", violation.getMessage());
    }

    @Test
    void testInvalidSetterType() {
        User user = new User("user@example.com", "USER");
        user.setType("INVALID_TYPE");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Invalid setter type should trigger validation errors.");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Type must be either 'USER' or 'ADMIN'", violation.getMessage());
    }
}
