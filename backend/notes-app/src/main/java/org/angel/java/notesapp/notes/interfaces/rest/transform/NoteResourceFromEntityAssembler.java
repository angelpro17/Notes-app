package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.angel.java.notesapp.notes.interfaces.rest.resources.NoteResource;

import java.util.stream.Collectors;

public class NoteResourceFromEntityAssembler {
    
    public static NoteResource toResourceFromEntity(Note entity) {
        return new NoteResource(
            entity.getId(),
            entity.getTitleValue(),
            entity.getContentValue(),
            entity.getArchived(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getCategories().stream()
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toSet())
        );
    }
}
