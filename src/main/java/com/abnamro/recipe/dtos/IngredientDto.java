package com.abnamro.recipe.dtos;

public record IngredientDto(
        String name,
        double amount,
        String unit
) {
}
