package com.abnamro.recipe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Positive
    private double amount;

    @Column(nullable = false)
    @NotBlank
    private String unit;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

}
