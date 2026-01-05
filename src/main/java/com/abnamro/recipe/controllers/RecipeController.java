package com.abnamro.recipe.controllers;

import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.RecipeDto;
import com.abnamro.recipe.dtos.SearchRecipe;
import com.abnamro.recipe.errors.ErrorResponse;
import com.abnamro.recipe.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Recipe")
@RestController
@RequestMapping("recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @Operation(summary = "Gets all recipes for the user",
            description = "User should be logged in, in order to fetch all their recipes " +
                    "which is a paginated list of recipes sorted by creation date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public Page<RecipeDto> getRecipes(@AuthenticationPrincipal UserDetails userDetails,
                                      @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return recipeService.getRecipes(userDetails.getUsername(), pageable);
    }

    @Operation(summary = "Create a new recipe",
            description = "Creates a new recipe for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody CreateOrUpdateRecipe recipe,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.createRecipe(recipe, userDetails.getUsername());
    }

    @Operation(summary = "Update an existing recipe",
            description = "Updates a recipe by ID. Only the owner of the recipe can update it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Does not own the recipe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{recipeId}")
    public RecipeDto updateRecipe(@PathVariable UUID recipeId,
                                  @Valid @RequestBody CreateOrUpdateRecipe recipe,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.updateRecipe(recipeId, recipe, userDetails.getUsername());
    }

    @Operation(summary = "Deletes a recipe",
            description = "Deletes a recipe by ID. Only the owner of the recipe can delete it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Does not own the recipe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable UUID recipeId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.deleteRecipe(recipeId, userDetails.getUsername());

    }

    @Operation(summary = "Search recipes",
            description = "Search recipes by various criteria including vegetarian option, servings, ingredients, and instructions." +
                    "Search is performed using and operation, Meaning the result will only include recipes that match all criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/search")
    public Page<RecipeDto> searchRecipes(@Valid @RequestBody SearchRecipe searchRecipe,
                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return recipeService.searchRecipes(searchRecipe, pageable);
    }

}
