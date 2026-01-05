package com.abnamro.recipe.repositories;

import com.abnamro.recipe.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID>, QuerydslPredicateExecutor<Recipe> {
    Page<Recipe> findByCreatedByUsername(String username, Pageable pageable);
}
