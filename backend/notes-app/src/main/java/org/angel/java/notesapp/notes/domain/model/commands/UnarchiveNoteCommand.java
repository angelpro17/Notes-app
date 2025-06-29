package org.angel.java.notesapp.notes.domain.model.commands;

public record UnarchiveNoteCommand(
    Long noteId,
    Long userId
) {
}
