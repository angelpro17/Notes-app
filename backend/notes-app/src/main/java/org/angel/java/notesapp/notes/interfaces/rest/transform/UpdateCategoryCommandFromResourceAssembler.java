package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.commands.UpdateCategoryCommand;
import org.angel.java.notesapp.notes.interfaces.rest.resources.UpdateCategoryResource;

public class UpdateCategoryCommandFromResourceAssembler {
    
    public static UpdateCategoryCommand toCommandFromResource(UpdateCategoryResource resource, Long categoryId, Long userId) {
        return new UpdateCategoryCommand(
            categoryId,
            resource.name(),
            resource.color(),
            userId
        );
    }
}
