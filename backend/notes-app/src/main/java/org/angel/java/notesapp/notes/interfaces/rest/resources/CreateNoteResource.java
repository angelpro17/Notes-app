package org.angel.java.notesapp.notes.interfaces.rest.resources;

import java.util.Set;

public record CreateNoteResource(
    String title,
    String content,
    Set<Long> categoryIds
) {
}
