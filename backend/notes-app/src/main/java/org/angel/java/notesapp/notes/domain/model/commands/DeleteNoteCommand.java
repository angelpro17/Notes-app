package org.angel.java.notesapp.notes.domain.model.commands;

public record DeleteNoteCommand(
    Long noteId,
    Long userId
) {
}
