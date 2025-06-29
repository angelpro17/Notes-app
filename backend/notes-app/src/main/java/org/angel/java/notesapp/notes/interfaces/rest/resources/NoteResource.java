package org.angel.java.notesapp.notes.interfaces.rest.resources;

import java.util.Date;
import java.util.Set;

public record NoteResource(
        Long id,
        String title,
        String content,
        Boolean archived,
        Date createdAt,
        Date updatedAt,
        Set<CategoryResource> categories
) {
}
