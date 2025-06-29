package org.angel.java.notesapp.notes.interfaces.rest.resources;

import java.util.Date;

public record CategoryResource(
        Long id,
        String name,
        String color,
        Date createdAt,
        Date updatedAt
) {
}
