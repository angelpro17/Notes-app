package org.angel.java.notesapp.notes.interfaces.acl;

import org.angel.java.notesapp.notes.domain.model.commands.CreateNoteCommand;
import org.angel.java.notesapp.notes.domain.model.queries.GetAllNotesByUserIdQuery;
import org.angel.java.notesapp.notes.domain.services.NoteCommandService;
import org.angel.java.notesapp.notes.domain.services.NoteQueryService;
import org.springframework.stereotype.Service;

@Service
public class NotesContextFacade {
    
    private final NoteCommandService noteCommandService;
    private final NoteQueryService noteQueryService;

    public NotesContextFacade(NoteCommandService noteCommandService, NoteQueryService noteQueryService) {
        this.noteCommandService = noteCommandService;
        this.noteQueryService = noteQueryService;
    }

    public Long createNote(String title, String content, Long userId) {
        var createNoteCommand = new CreateNoteCommand(title, content, userId, null);
        return noteCommandService.handle(createNoteCommand);
    }

    public int fetchNoteCountByUserId(Long userId) {
        var getAllNotesQuery = new GetAllNotesByUserIdQuery(userId);
        var notes = noteQueryService.handle(getAllNotesQuery);
        return notes.size();
    }
}
