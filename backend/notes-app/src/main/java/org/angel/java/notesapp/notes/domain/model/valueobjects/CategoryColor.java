package org.angel.java.notesapp.notes.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record CategoryColor(
    @Column(name = "color", length = 7)
    String color
) {
    public CategoryColor {
        if (color != null && !color.matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Category color must be a valid hex color (e.g., #3f51b5)");
        }
    }
}
