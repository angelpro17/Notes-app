package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.commands.CreateNoteCommand;
import org.angel.java.notesapp.notes.interfaces.rest.resources.CreateNoteResource;

public class CreateNoteCommandFromResourceAssembler {
    
    public static CreateNoteCommand toCommandFromResource(CreateNoteResource resource, Long userId) {
        return new CreateNoteCommand(
            resource.title(),
            resource.content(),
            userId,
            resource.categoryIds()
        );
    }
}
