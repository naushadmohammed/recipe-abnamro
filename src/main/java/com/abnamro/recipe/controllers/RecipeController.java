package com.abnamro.recipe.controllers;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody CreateOrUpdateRecipe recipe,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.createRecipe(recipe, userDetails.getUsername());
    }

}
