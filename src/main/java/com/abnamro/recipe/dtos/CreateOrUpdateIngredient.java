package com.abnamro.recipe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrUpdateIngredient(
        @NotBlank
        String name,

        @Positive
        double amount,

        @NotBlank
        String unit
) {
}
