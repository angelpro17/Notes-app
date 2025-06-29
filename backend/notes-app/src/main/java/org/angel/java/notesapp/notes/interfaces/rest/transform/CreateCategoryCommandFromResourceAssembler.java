package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.commands.CreateCategoryCommand;
import org.angel.java.notesapp.notes.interfaces.rest.resources.CreateCategoryResource;

public class CreateCategoryCommandFromResourceAssembler {
    
    public static CreateCategoryCommand toCommandFromResource(CreateCategoryResource resource, Long userId) {
        return new CreateCategoryCommand(
            resource.name(),
            resource.color(),
            userId
        );
    }
}
