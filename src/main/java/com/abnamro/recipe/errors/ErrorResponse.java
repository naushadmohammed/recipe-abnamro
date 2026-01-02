package com.abnamro.recipe.errors;

import java.util.List;

public record ErrorResponse(int status, List<String> message) {
}
