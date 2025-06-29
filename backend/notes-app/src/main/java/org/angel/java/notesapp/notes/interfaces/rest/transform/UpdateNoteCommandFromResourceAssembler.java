package org.angel.java.notesapp.notes.interfaces.rest.transform;

import org.angel.java.notesapp.notes.domain.model.commands.UpdateNoteCommand;
import org.angel.java.notesapp.notes.interfaces.rest.resources.UpdateNoteResource;

public class UpdateNoteCommandFromResourceAssembler {
    
    public static UpdateNoteCommand toCommandFromResource(UpdateNoteResource resource, Long noteId, Long userId) {
        return new UpdateNoteCommand(
            noteId,
            resource.title(),
            resource.content(),
            userId,
            resource.categoryIds()
        );
    }
}
