package com.abnamro.recipe.utils;

import com.abnamro.recipe.dtos.*;

import java.util.List;

public class TestUtil {

    public static UserDto getUser1() {
        return new UserDto("user1", "user1Passowrd");
    }

    public static UserDto getUser2() {
        return new UserDto("user2", "user2Passowrd");
    }

    public static CreateOrUpdateRecipe getValidRecipe() {
        return new CreateOrUpdateRecipe(
                "Chicken Tikka",
                "Chicken Tikka Masala",
                4,
                RecipeType.NON_VEGETARIAN,
                getChickenTikkaIngedient(),
                getChickenTikkaInstructions()
                );
    }

    public static CreateOrUpdateRecipe getValidRecipeWithouthIngredientsAndInstructions() {
        return new CreateOrUpdateRecipe(
                "Chicken Tikka Gravy",
                "Chicken Tikka Masala with gravy",
                5,
                RecipeType.NON_VEGETARIAN,
                null,null
        );
    }

    public static CreateOrUpdateRecipe getInvalidRecipe() {
        return new CreateOrUpdateRecipe(
                "",
                "Chicken Tikka Masala",
                4,
                RecipeType.NON_VEGETARIAN,
                getChickenTikkaIngedient(),
                getChickenTikkaInstructions()
        );
    }


    public static List<CreateOrUpdateIngredient> getChickenTikkaIngedient(){
        var chicken = new CreateOrUpdateIngredient("Chicken", 1, "kg");
        var onion = new CreateOrUpdateIngredient("Onion", 1, "kg");
        var garlic = new CreateOrUpdateIngredient("Garlic", 100, "gram");
        var ginger = new CreateOrUpdateIngredient("Ginger", 100, "gram");
        return List.of(chicken, onion, garlic, ginger);
    }

    public static List<CreateOrUpdateInstruction> getChickenTikkaInstructions(){
        var cutChicken = new CreateOrUpdateInstruction("Cut chicken");
        var fry = new CreateOrUpdateInstruction("Fry onion, garlic and ginger");
        var addChicken = new CreateOrUpdateInstruction("Add chicken");
        return List.of(cutChicken, fry, addChicken);
    }
}
