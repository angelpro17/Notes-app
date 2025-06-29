package org.angel.java.notesapp.notes.domain.model.commands;

import java.util.Set;

public record CreateNoteCommand(
    String title,
    String content,
    Long userId,
    Set<Long> categoryIds
) {
}
