package org.angel.java.notesapp.notes.application.internal.queryservices;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.angel.java.notesapp.notes.domain.model.queries.*;
import org.angel.java.notesapp.notes.domain.services.NoteQueryService;
import org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteQueryServiceImpl implements NoteQueryService {

    private final NoteRepository noteRepository;

    public NoteQueryServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> handle(GetAllNotesByUserIdQuery query) {
        return noteRepository.findByUserIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public Optional<Note> handle(GetNoteByIdAndUserIdQuery query) {
        return noteRepository.findByIdAndUserId(query.noteId(), query.userId());
    }

    @Override
    public List<Note> handle(GetActiveNotesByUserIdQuery query) {
        return noteRepository.findByUserIdAndArchivedOrderByCreatedAtDesc(query.userId(), false);
    }

    @Override
    public List<Note> handle(GetArchivedNotesByUserIdQuery query) {
        return noteRepository.findByUserIdAndArchivedOrderByCreatedAtDesc(query.userId(), true);
    }

    @Override
    public List<Note> handle(GetNotesByCategoryAndUserIdQuery query) {
        return noteRepository.findByUserIdAndCategoryId(query.userId(), query.categoryId());
    }

    @Override
    public List<Note> handle(SearchNotesByUserIdQuery query) {
        return noteRepository.findByUserIdAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            query.userId(), query.searchTerm());
    }
}
