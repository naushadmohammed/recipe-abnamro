package com.abnamro.recipe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SearchRecipe(
        Boolean isVegetarian,

        @Positive
        Integer noOfServings,

        List<@NotBlank String> includingIngredients,
        List<@NotBlank String> excludingIngredients,

        String includingInstruction
) {
}
