package com.klp.oppgave.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUserValid() throws Exception {
        User user = new User("valid.email@example.com", "USER");
        when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content("{\"email\":\"valid.email@example.com\",\"type\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("valid.email@example.com"))
                .andExpect(jsonPath("$.type").value("USER"));
    }

    @Test
    void testCreateUserInvalid() throws Exception {
        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content("{\"email\":\"invalid\",\"type\":\"USER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: Email must be a valid email address"));
    }

    @Test
    void testCreateUserIncomplete() throws Exception {
        String invalidUserJson = """
        {
            "email": "test@example.com"
        }
    """;

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: Type cannot be null"));
    }

    @Test
    void testGetUserById() throws Exception {
        User mockUser = new User("test@example.com", "USER");
        when(userService.getUserById(1)).thenReturn(mockUser);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.type").value("USER"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        int nonExistentUserId = 999;
        when(userService.getUserById(nonExistentUserId))
                .thenThrow(new EntityNotFoundException("User with ID " + nonExistentUserId + " not found"));

        mockMvc.perform(get("/user/" + nonExistentUserId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID 999 not found"));
    }

    @Test
    void testGetUserWithInvalidId() throws Exception {
        mockMvc.perform(get("/user/abc")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ID must be an integer"));
    }

    @Test
    void testGetUsersWithFilter() throws Exception {
        User mockUser1 = new User("user1@example.com", "USER");
        User mockUser2 = new User("user2@example.com", "USER");
        when(userService.getUsersByType("USER")).thenReturn(List.of(mockUser1, mockUser2));

        mockMvc.perform(get("/user")
                        .param("type-filter", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));
    }

    @Test
    void testGetUsersWithoutFilter() throws Exception {
        User mockUser1 = new User("user@example.com", "USER");
        User mockUser2 = new User("admin@example.com", "ADMIN");
        when(userService.getUsersByType(null)).thenReturn(List.of(mockUser1, mockUser2));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"))
                .andExpect(jsonPath("$[0].type").value("USER"))
                .andExpect(jsonPath("$[1].email").value("admin@example.com"))
                .andExpect(jsonPath("$[1].type").value("ADMIN"));
    }
}

