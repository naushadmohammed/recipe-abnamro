package com.abnamro.recipe.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrUpdateRecipe(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @Min(1)
        int servings,

        @NotNull
        RecipeType type,

        @Valid
        List<CreateOrUpdateIngredient> ingredients,

        @Valid
        List<CreateOrUpdateInstruction> instructions
) {
}
