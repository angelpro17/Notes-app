package org.angel.java.notesapp.notes.domain.model.queries;

public record SearchNotesByUserIdQuery(String searchTerm, Long userId) {
}
