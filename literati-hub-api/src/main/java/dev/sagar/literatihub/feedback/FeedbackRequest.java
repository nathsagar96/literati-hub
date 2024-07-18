package dev.sagar.literatihub.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FeedbackRequest(
    @Positive(message = "Note must be a positive number.")
        @Min(value = 0, message = "Note must be greater than or equal to 0.")
        @Max(value = 5, message = "Note must be less than or equal to 5.")
        Double note,
    @NotNull(message = "Comment cannot be null.")
        @NotEmpty(message = "Comment cannot be empty.")
        @NotBlank(message = "Comment cannot be blank.")
        String comment,
    @NotBlank(message = "Book ID cannot be blank.") Integer bookId) {}
