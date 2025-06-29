package org.angel.java.notesapp.notes.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
public record NoteContent(
    @NotBlank(message = "Note content cannot be blank")
    @Size(max = 1000, message = "Note content cannot exceed 1000 characters")
    @Column(name = "content", length = 1000, nullable = false)
    String content
) {
    public NoteContent {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Note content cannot be null or blank");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Note content cannot exceed 1000 characters");
        }
    }
}
