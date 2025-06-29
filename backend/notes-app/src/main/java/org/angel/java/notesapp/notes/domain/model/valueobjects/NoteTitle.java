package org.angel.java.notesapp.notes.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public record NoteTitle(
    @NotBlank(message = "Note title cannot be blank")
    @Size(max = 100, message = "Note title cannot exceed 100 characters")
    @Column(name = "title", length = 100, nullable = false)
    String title
) {
    public NoteTitle {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Note title cannot be null or blank");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Note title cannot exceed 100 characters");
        }
    }
}
