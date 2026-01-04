package com.abnamro.recipe.mappers;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.entities.Recipe;
import org.mapstruct.*;

import java.util.concurrent.atomic.AtomicInteger;

@Mapper(
        componentModel = "spring",
        uses = {IngredientMapper.class, InstructionMapper.class}
)
public interface RecipeMapper {

    Recipe toEntity(CreateOrUpdateRecipe dto);

    @Mapping(target = "createdBy", source = "createdBy.username")
    RecipeDto toDto(Recipe entity);

    @AfterMapping
    default void addIngredientsAndInstructions(@MappingTarget Recipe recipe) {
        if (recipe.getIngredients() != null) {
            recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe));
        }
        if (recipe.getInstructions() != null) {
            var step = new AtomicInteger(1);
            recipe.getInstructions().forEach(instruction -> {
                instruction.setRecipe(recipe);
                instruction.setStep(step.getAndIncrement());
            });
        }
    }
}
