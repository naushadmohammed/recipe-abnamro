package com.abnamro.recipe.controllers;

import com.abnamro.recipe.dtos.UserDto;
import com.abnamro.recipe.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody UserDto createUser) {
        userService.createUser(createUser);
        return "User created successfully";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody UserDto user) {
        return userService.loginUser(user);
    }
}