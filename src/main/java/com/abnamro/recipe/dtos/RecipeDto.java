package com.abnamro.recipe.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RecipeDto(
        UUID id,
        String name,
        String description,
        int servings,
        RecipeType type,
        List<IngredientDto> ingredients,
        List<InstructionDto> instructions,
        String createdBy,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
}
