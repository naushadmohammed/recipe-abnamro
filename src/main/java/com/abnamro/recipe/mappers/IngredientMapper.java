package com.abnamro.recipe.mappers;

import com.abnamro.recipe.dtos.CreateOrUpdateIngredient;
import com.abnamro.recipe.entities.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(CreateOrUpdateIngredient dto);
}