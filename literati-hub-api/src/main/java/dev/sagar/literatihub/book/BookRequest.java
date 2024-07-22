package dev.sagar.literatihub.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
    Integer id,
    @NotNull(message = "Title is required") @NotEmpty(message = "Title cannot be empty")
        String title,
    @NotNull(message = "Author name is required") @NotEmpty(message = "Author name cannot be empty")
        String authorName,
    @NotNull(message = "ISBN is required") @NotEmpty(message = "ISBN cannot be empty") String isbn,
    @NotNull(message = "Synopsis is required") @NotEmpty(message = "Synopsis cannot be empty")
        String synopsis,
    Boolean shareable) {}
