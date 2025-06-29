package org.angel.java.notesapp.notes.domain.model.commands;

import java.util.Set;

public record UpdateNoteCommand(
    Long noteId,
    String title,
    String content,
    Long userId,
    Set<Long> categoryIds
) {
}
