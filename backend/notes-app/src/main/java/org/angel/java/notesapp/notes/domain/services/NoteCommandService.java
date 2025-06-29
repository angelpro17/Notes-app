package org.angel.java.notesapp.notes.domain.services;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.angel.java.notesapp.notes.domain.model.commands.*;

import java.util.Optional;

public interface NoteCommandService {
    Long handle(CreateNoteCommand command);
    Optional<Note> handle(UpdateNoteCommand command);
    Optional<Note> handle(ArchiveNoteCommand command);
    Optional<Note> handle(UnarchiveNoteCommand command);
    void handle(DeleteNoteCommand command);
}
