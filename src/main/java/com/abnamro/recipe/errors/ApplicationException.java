package com.abnamro.recipe.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus status;

    public ApplicationException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}