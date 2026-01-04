package com.abnamro.recipe.dtos;

public enum RecipeType {
    VEGAN("vegan"),
    VEGETARIAN("vegetarian"),
    NON_VEGETARIAN("non-vegetarian");
    private final String name;

    RecipeType(String name) {
        this.name = name;
    }
}
