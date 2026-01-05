package com.abnamro.recipe.services;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.dtos.SearchRecipe;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.mappers.RecipeMapper;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.utils.RecipeSearchBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public Page<RecipeDto> getRecipes(String username, Pageable pageable) {
        return recipeRepository.findByCreatedByUsername(username, pageable)
                .map(recipeMapper::toDto);
    }

    @PreAuthorize("@recipeSecurityService.isRecipeOwner(#username, #recipeId)")
    public RecipeDto updateRecipe(UUID recipeId, CreateOrUpdateRecipe recipe, String username) {
        var existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Recipe not found"));

        recipeMapper.updateEntityFromDto(recipe, existingRecipe);
        existingRecipe.setUpdatedAt(LocalDateTime.now());
        var updatedRecipe = recipeRepository.save(existingRecipe);
        return recipeMapper.toDto(updatedRecipe);
    }

    @PreAuthorize("@recipeSecurityService.isRecipeOwner(#username, #recipeId)")
    public void deleteRecipe(UUID recipeId, String username) {

        recipeRepository.deleteById(recipeId);
    }

    public Page<RecipeDto> searchRecipes(SearchRecipe searchRecipe, Pageable pageable) {

        var searchQuery = RecipeSearchBuilder.buildSearchQuery(searchRecipe);

        return recipeRepository.findAll(searchQuery, pageable)
                .map(recipeMapper::toDto);
    }
}
