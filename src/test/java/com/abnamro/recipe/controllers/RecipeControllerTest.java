package com.abnamro.recipe.controllers;

import com.abnamro.recipe.TestcontainersConfiguration;
import com.abnamro.recipe.dtos.CreateOrUpdateRecipe;
import com.abnamro.recipe.dtos.SearchRecipe;
import com.abnamro.recipe.repositories.RecipeRepository;
import com.abnamro.recipe.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.abnamro.recipe.utils.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String authTokenUser1;
    private static String authTokenUser2;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var user1 = getUser1();
        userService.createUser(user1);
        authTokenUser1 = userService.loginUser(user1);

        var user2 = getUser2();
        userService.createUser(user2);
        authTokenUser2 = userService.loginUser(user2);

    }

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();

    }

    @Test
    @Transactional
    void create_recipe_work_for_authenticated_user() throws Exception {
        var recipe = getValidRecipe();

        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        assertThat(createdRecipe.getName()).isEqualTo(recipe.name());
        assertThat(createdRecipe.getDescription()).isEqualTo(recipe.description());
        assertThat(createdRecipe.getServings()).isEqualTo(recipe.servings());
        assertThat(createdRecipe.getType()).isEqualTo(recipe.type());
        assertThat(createdRecipe.getCreatedBy().getUsername()).isEqualTo(getUser1().username());
        assertThat(createdRecipe.getCreatedAt()).isNotNull();

        assertThat(createdRecipe.getIngredients()).hasSize(recipe.ingredients().size());
        assertThat(createdRecipe.getInstructions()).hasSize(recipe.instructions().size());

        for (int i = 0; i < recipe.ingredients().size(); i++) {
            var ingredient = recipe.ingredients().get(i);
            var createdIngredient = createdRecipe.getIngredients().get(i);

            assertThat(createdIngredient.getName()).isEqualTo(ingredient.name());
            assertThat(createdIngredient.getAmount()).isEqualTo(ingredient.amount());
            assertThat(createdIngredient.getUnit()).isEqualTo(ingredient.unit());
        }

        for (int i = 0; i < recipe.instructions().size(); i++) {
            var instruction = recipe.instructions().get(i);
            var createdInstruction = createdRecipe.getInstructions().get(i);

            assertThat(createdInstruction.getStep()).isEqualTo(i + 1);
            assertThat(createdInstruction.getDescription()).isEqualTo(instruction.description());
        }
    }

    @Test
    void create_recipe_fail_for_unauthenticated_user() throws Exception {
        var recipe = getValidRecipe();

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_recipe_fails_with_empty_name() throws Exception {
        var recipe = getInvalidRecipe();

        mockMvc.perform(post("/recipe")
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void create_recipe_works_without_ingredients_and_instructions() throws Exception {
        var recipe = getValidRecipeWithouthIngredientsAndInstructions();

        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        assertThat(createdRecipe.getName()).isEqualTo(recipe.name());
        assertThat(createdRecipe.getDescription()).isEqualTo(recipe.description());
        assertThat(createdRecipe.getServings()).isEqualTo(recipe.servings());
        assertThat(createdRecipe.getType()).isEqualTo(recipe.type());

        assertThat(createdRecipe.getIngredients()).isNull();
        assertThat(createdRecipe.getInstructions()).isNull();

    }

    @Test
    void getRecipes_should_only_return_recipes_for_logged_in_user() throws Exception {
        var recipe1 = getValidRecipe();
        createRecipeUsingMockMvc(recipe1);

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        createRecipeUsingMockMvc(recipe2);

        var recipe3 = getValidRecipe();
        mockMvc.perform(post("/recipe")
                        .header("Authorization", "Bearer " + authTokenUser2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/recipe")
                        .header("Authorization", "Bearer " + authTokenUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void getRecipes_should_return_pageable_recipes() throws Exception {
        var recipe1 = getValidRecipe();
        createRecipeUsingMockMvc(recipe1);

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        createRecipeUsingMockMvc(recipe2);

        var recipe3 = getValidRecipe();
        createRecipeUsingMockMvc(recipe3);

        mockMvc.perform(get("/recipe?page=0&size=2")
                        .header("Authorization", "Bearer " + authTokenUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(3));
    }

    @Test
    void update_own_recipe_successfully() throws Exception {
        var recipe = getValidRecipe();
        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        var recipeId = createdRecipe.getId();

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        mockMvc.perform(put("/recipe/{recipeId}", recipeId)
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe2)))
                .andExpect(status().isOk());
        var updatedRecipe = recipeRepository.findById(recipeId).orElseThrow();
        assertThat(updatedRecipe.getName()).isEqualTo(recipe2.name());
        assertThat(updatedRecipe.getDescription()).isEqualTo(recipe2.description());
        assertThat(updatedRecipe.getServings()).isEqualTo(recipe2.servings());
        assertThat(updatedRecipe.getType()).isEqualTo(recipe2.type());
        assertThat(updatedRecipe.getCreatedBy().getUsername()).isEqualTo(getUser1().username());
    }

    @Test
    void updating_others_recipe_should_fail() throws Exception {
        var recipe = getValidRecipe();
        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        var recipeId = createdRecipe.getId();

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        mockMvc.perform(put("/recipe/{recipeId}", recipeId)
                        .header("Authorization", "Bearer " + authTokenUser2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe2)))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete_own_recipe_successfully() throws Exception {
        var recipe = getValidRecipe();
        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        var recipeId = createdRecipe.getId();

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        mockMvc.perform(delete("/recipe/{recipeId}", recipeId)
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe2)))
                .andExpect(status().isNoContent());
        var deletedRecipe = recipeRepository.findById(recipeId).orElse(null);

        assertThat(deletedRecipe).isNull();
    }

    @Test
    void deleting_others_recipe_should_fail() throws Exception {
        var recipe = getValidRecipe();
        createRecipeUsingMockMvc(recipe);

        var createdRecipe = recipeRepository.findAll().getFirst();
        var recipeId = createdRecipe.getId();

        var recipe2 = getValidRecipeWithouthIngredientsAndInstructions();
        mockMvc.perform(delete("/recipe/{recipeId}", recipeId)
                        .header("Authorization", "Bearer " + authTokenUser2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe2)))
                .andExpect(status().isForbidden());
        var recipeNotDeleted = recipeRepository.findById(recipeId).orElse(null);

        assertThat(recipeNotDeleted).isNotNull();
    }


    @Test
    void search_for_veg_recipe_order_by_created_date() throws Exception {
        var paneeraRecipe = getPaneeraRecipe();
        createRecipeUsingMockMvc(paneeraRecipe);

        var pastaRecipe = getVegPastaRecipe();
        createRecipeUsingMockMvc(pastaRecipe);

        var chickenRecipe = getValidRecipe();
        createRecipeUsingMockMvc(chickenRecipe);

        var searchRequest = new SearchRecipe(true, null, null, null, null);

        mockMvc.perform(post("/recipe/search")
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value(pastaRecipe.name()))
                .andExpect(jsonPath("$.content[1].name").value(paneeraRecipe.name()));
    }

    @Test
    void search_for_veg_recipe_which_serves_6_and_contains_paneer() throws Exception {
        var paneeraRecipe = getPaneeraRecipe();
        createRecipeUsingMockMvc(paneeraRecipe);

        var pastaRecipe = getVegPastaRecipe();
        createRecipeUsingMockMvc(pastaRecipe);

        var searchRequest = new SearchRecipe(true, 6, List.of("Paneer"), null, null);

        mockMvc.perform(post("/recipe/search")
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value(paneeraRecipe.name()));
    }

    @Test
    void search_for_recipe_which_doesnt_contain_paneer_has_boil_instructions() throws Exception {
        var paneeraRecipe = getPaneeraRecipe();
        createRecipeUsingMockMvc(paneeraRecipe);

        var pastaRecipe = getVegPastaRecipe();
        createRecipeUsingMockMvc(pastaRecipe);

        var chickenRecipe = getValidRecipe();
        createRecipeUsingMockMvc(chickenRecipe);

        var searchRequest = new SearchRecipe(null, null, null, List.of("Paneer"), "Boil");

        mockMvc.perform(post("/recipe/search")
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value(chickenRecipe.name()))
                .andExpect(jsonPath("$.content[1].name").value(pastaRecipe.name()));

    }


    private void createRecipeUsingMockMvc(CreateOrUpdateRecipe recipe) throws Exception {
        mockMvc.perform(post("/recipe")
                        .header("Authorization", "Bearer " + authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());
    }
}
