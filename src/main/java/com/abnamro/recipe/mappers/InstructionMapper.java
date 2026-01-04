package com.abnamro.recipe.mappers;

import com.abnamro.recipe.dtos.CreateOrUpdateInstruction;
import com.abnamro.recipe.entities.Instruction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructionMapper {

    Instruction toEntity(CreateOrUpdateInstruction dto);
}