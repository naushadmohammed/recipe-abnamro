package com.abnamro.recipe.utils;

import com.abnamro.recipe.dtos.RecipeType;
import com.abnamro.recipe.dtos.SearchRecipe;
import com.abnamro.recipe.entities.QRecipe;
import com.querydsl.core.BooleanBuilder;

public class RecipeSearchBuilder {

    private static final QRecipe recipe = QRecipe.recipe;

    public static BooleanBuilder buildSearchQuery(SearchRecipe searchRecipe) {

        var builder = new BooleanBuilder();

        if (searchRecipe.isVegetarian() != null) {
            builder.and(searchRecipe.isVegetarian() ? recipe.type.in(RecipeType.VEGETARIAN, RecipeType.VEGAN)
                    : recipe.type.eq(RecipeType.NON_VEGETARIAN));
        }

        if (searchRecipe.noOfServings() != null) {
            builder.and(recipe.servings.eq(searchRecipe.noOfServings()));
        }

        if (searchRecipe.includingIngredients() != null) {
            builder.and(recipe.ingredients.any().name.in(searchRecipe.includingIngredients()));
        }

        if (searchRecipe.excludingIngredients() != null) {
            builder.and(recipe.ingredients.any().name.notIn(searchRecipe.excludingIngredients()));

        }

        if (searchRecipe.includingInstruction() != null) {
            builder.and(recipe.instructions.any().description.containsIgnoreCase(searchRecipe.includingInstruction()));
        }

        return builder;
    }
}