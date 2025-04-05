package com.klp.oppgave.user.controller;

import com.klp.oppgave.user.entity.User;
import com.klp.oppgave.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(value = "type-filter", required = false) String filter) {
        return userService.getUsersByType(filter);
    }
}
