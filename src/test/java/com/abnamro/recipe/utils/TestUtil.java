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

    public static CreateOrUpdateRecipe getVegPastaRecipe() {
        return new CreateOrUpdateRecipe(
                "Veg Pasta",
                "Veg Pasta with parmesaan cheese",
                6,
                RecipeType.VEGETARIAN,
                getPastaIngedient(),
                getPastaInstructions()
        );
    }

    public static CreateOrUpdateRecipe getPaneeraRecipe() {
        return new CreateOrUpdateRecipe(
                "Veg Paneer",
                "Veg Paneer tikka",
                6,
                RecipeType.VEGETARIAN,
                getPaneerIngedient(),
                getPaneerInstructions()
        );
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
        var cutChicken = new CreateOrUpdateInstruction("Cut chicken and Boil");
        var fry = new CreateOrUpdateInstruction("Fry onion, garlic and ginger");
        var addChicken = new CreateOrUpdateInstruction("Add chicken");
        return List.of(cutChicken, fry, addChicken);
    }

    public static List<CreateOrUpdateIngredient> getPastaIngedient(){
        var pasta = new CreateOrUpdateIngredient("Pasta", 1, "kg");
        var onion = new CreateOrUpdateIngredient("Onion", 1, "kg");
        var garlic = new CreateOrUpdateIngredient("Garlic", 100, "gram");
        var cheese = new CreateOrUpdateIngredient("Cheese", 100, "gram");
        var tomato = new CreateOrUpdateIngredient("Tomato", 1, "kg");
        return List.of(pasta, onion, garlic, cheese, tomato);
    }

    public static List<CreateOrUpdateInstruction> getPastaInstructions(){
        var boilPasta = new CreateOrUpdateInstruction("Boil pasta");
        var fry = new CreateOrUpdateInstruction("Fry onion, garlic, cheese and tomato");
        var putPasta = new CreateOrUpdateInstruction("Put pasta in the pan");
        return List.of(boilPasta, fry, putPasta);
    }

    public static List<CreateOrUpdateIngredient> getPaneerIngedient(){
        var pasta = new CreateOrUpdateIngredient("Paneer", 1, "kg");
        var onion = new CreateOrUpdateIngredient("Onion", 1, "kg");
        var garlic = new CreateOrUpdateIngredient("Garlic", 100, "gram");
        var tomato = new CreateOrUpdateIngredient("Tomato", 1, "kg");
        return List.of(pasta, onion, garlic, tomato);
    }

    public static List<CreateOrUpdateInstruction> getPaneerInstructions(){
        var cutEverything = new CreateOrUpdateInstruction("Cut onion, pasta, paneer, garlic and tomato");
        var fry = new CreateOrUpdateInstruction("Fry onion, garlic and tomato");
        var putPaneer = new CreateOrUpdateInstruction("Put paneer in the pan");
        var shimmer = new CreateOrUpdateInstruction("Let it Shimmer for 10 minutes");
        return List.of(cutEverything, fry, putPaneer, shimmer);
    }
}
