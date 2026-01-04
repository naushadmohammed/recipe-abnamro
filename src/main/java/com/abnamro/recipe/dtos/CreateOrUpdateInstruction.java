package com.abnamro.recipe.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateInstruction(
        @NotBlank
        String description
) {
}
