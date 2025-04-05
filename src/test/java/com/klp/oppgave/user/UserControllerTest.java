package com.klp.oppgave.user;

import com.klp.oppgave.user.controller.UserController;
import com.klp.oppgave.user.entity.User;
import com.klp.oppgave.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
    void testCreateUser() throws Exception {
        User mockUser = new User("test@example.com", "USER");
        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        String userJson = "{ \"email\": \"test@example.com\", \"type\": \"USER\" }";

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.type").value("USER"));
    }

    @Test
    void testGetUserById() throws Exception {
        User mockUser = new User("test@example.com", "USER");
        when(userService.getUserById(1)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.type").value("USER"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/999"))
                .andExpect(status().isNotFound());
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

