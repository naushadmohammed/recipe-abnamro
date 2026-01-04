package com.abnamro.recipe.services;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.mappers.RecipeMapper;
import com.abnamro.recipe.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserService userService;

    public RecipeDto createRecipe(CreateOrUpdateRecipe recipe, String username) {
        var recipeEntity = recipeMapper.toEntity(recipe);

        var user = userService.getUserByUsername(username);
        recipeEntity.setCreatedBy(user);

        var now = LocalDateTime.now();
        recipeEntity.setCreatedAt(now);
        recipeEntity.setUpdatedAt(now);

        var createdRecipe = recipeRepository.save(recipeEntity);

        return recipeMapper.toDto(createdRecipe);
    }
}
