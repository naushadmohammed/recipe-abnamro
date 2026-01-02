package com.abnamro.recipe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank(message = "Username is required")
        @Size(min = 5, message = "Username should contain minimum 5 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password should contain minimum 8 characters")
        String password
) {

}