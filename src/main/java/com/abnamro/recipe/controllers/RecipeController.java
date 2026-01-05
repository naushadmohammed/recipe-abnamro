package com.abnamro.recipe.controllers;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.dtos.SearchRecipe;
import com.abnamro.recipe.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public Page<RecipeDto> getRecipes(@AuthenticationPrincipal UserDetails userDetails,
                                      @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return recipeService.getRecipes(userDetails.getUsername(), pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody CreateOrUpdateRecipe recipe,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.createRecipe(recipe, userDetails.getUsername());
    }

    @PutMapping("/{recipeId}")
    public RecipeDto updateRecipe(@PathVariable UUID recipeId,
                                  @Valid @RequestBody CreateOrUpdateRecipe recipe,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.updateRecipe(recipeId, recipe, userDetails.getUsername());
    }

    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable UUID recipeId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.deleteRecipe(recipeId, userDetails.getUsername());

    }

    @PostMapping("/search")
    public Page<RecipeDto> searchRecipes(@Valid @RequestBody SearchRecipe searchRecipe,
                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return recipeService.searchRecipes(searchRecipe, pageable);
    }

}
