package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.interfaces.rest.resources.CategoryResource;

public class CategoryResourceFromEntityAssembler {
    
    public static CategoryResource toResourceFromEntity(Category entity) {
        return new CategoryResource(
            entity.getId(),
            entity.getNameValue(),
            entity.getColorValue(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
