package com.abnamro.recipe.controllers;

import com.abnamro.recipe.TestcontainersConfiguration;
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

import static com.abnamro.recipe.utils.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private static String authToken;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        var user = getUser1();
        userService.createUser(user);
        authToken = userService.loginUser(user);
    }

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    @Transactional
    void create_recipe_work_for_authenticated_user() throws Exception {
        var recipe = getValidRecipe();

        mockMvc.perform(post("/recipe")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

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
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void create_recipe_works_without_ingredients_and_instructions() throws Exception {
        var recipe = getValidRecipeWithouthIngredientsAndInstructions();

        mockMvc.perform(post("/recipe")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

        var createdRecipe = recipeRepository.findAll().getFirst();
        assertThat(createdRecipe.getName()).isEqualTo(recipe.name());
        assertThat(createdRecipe.getDescription()).isEqualTo(recipe.description());
        assertThat(createdRecipe.getServings()).isEqualTo(recipe.servings());
        assertThat(createdRecipe.getType()).isEqualTo(recipe.type());

        assertThat(createdRecipe.getIngredients()).isNull();
        assertThat(createdRecipe.getInstructions()).isNull();

    }

}
