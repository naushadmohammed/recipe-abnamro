package com.abnamro.recipe.security;

import com.abnamro.recipe.entities.Recipe;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeSecurityService {
    private final RecipeRepository recipeRepository;

    public boolean isRecipeOwner(String username, UUID recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Recipe not found"));
        return recipe.getCreatedBy().getUsername().equals(username);
    }
}
