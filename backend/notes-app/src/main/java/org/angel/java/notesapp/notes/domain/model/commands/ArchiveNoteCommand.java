package org.angel.java.notesapp.notes.domain.model.commands;

public record ArchiveNoteCommand(
    Long noteId,
    Long userId
) {
}
