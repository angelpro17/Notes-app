package org.angel.java.notesapp.notes.interfaces.rest.resources;

import java.util.Set;

public record UpdateNoteResource(
    String title,
    String content,
    Set<Long> categoryIds
) {
}
