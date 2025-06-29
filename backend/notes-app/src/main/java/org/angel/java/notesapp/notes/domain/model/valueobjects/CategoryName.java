package org.angel.java.notesapp.notes.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public record CategoryName(
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    @Column(name = "name", length = 50, nullable = false)
    String name
) {
    public CategoryName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or blank");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Category name cannot exceed 50 characters");
        }
    }
}
