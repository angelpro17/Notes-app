package org.angel.java.notesapp.notes.domain.services;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.angel.java.notesapp.notes.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface NoteQueryService {
    List<Note> handle(GetAllNotesByUserIdQuery query);
    Optional<Note> handle(GetNoteByIdAndUserIdQuery query);
    List<Note> handle(GetActiveNotesByUserIdQuery query);
    List<Note> handle(GetArchivedNotesByUserIdQuery query);
    List<Note> handle(GetNotesByCategoryAndUserIdQuery query);
    List<Note> handle(SearchNotesByUserIdQuery query);
}
