package com.abnamro.recipe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "instructions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Min(1)
    private int step;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

}
