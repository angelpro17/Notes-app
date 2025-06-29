package org.angel.java.notesapp.notes.application.internal.commandservices;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.angel.java.notesapp.notes.domain.model.commands.*;
import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.domain.services.NoteCommandService;
import org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories.CategoryRepository;
import org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class NoteCommandServiceImpl implements NoteCommandService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;

    public NoteCommandServiceImpl(NoteRepository noteRepository, CategoryRepository categoryRepository) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Long handle(CreateNoteCommand command) {
        Set<Category> categories = new HashSet<>();
        if (command.categoryIds() != null && !command.categoryIds().isEmpty()) {
            categories = new HashSet<>(categoryRepository.findAllById(command.categoryIds()));
            // Filter categories that belong to the user
            categories.removeIf(category -> !category.getUserId().equals(command.userId()));
        }
        
        Note note = new Note(command.title(), command.content(), command.userId(), categories);
        Note savedNote = noteRepository.save(note);
        return savedNote.getId();
    }

    @Override
    @Transactional
    public Optional<Note> handle(UpdateNoteCommand command) {
        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(command.noteId(), command.userId());
        
        if (noteOptional.isEmpty()) {
            return Optional.empty();
        }
        
        Note note = noteOptional.get();
        note.updateTitle(command.title());
        note.updateContent(command.content());
        
        // Update categories
        Set<Category> categories = new HashSet<>();
        if (command.categoryIds() != null && !command.categoryIds().isEmpty()) {
            categories = new HashSet<>(categoryRepository.findAllById(command.categoryIds()));
            // Filter categories that belong to the user
            categories.removeIf(category -> !category.getUserId().equals(command.userId()));
        }
        note.updateCategories(categories);
        
        Note savedNote = noteRepository.save(note);
        return Optional.of(savedNote);
    }

    @Override
    @Transactional
    public Optional<Note> handle(ArchiveNoteCommand command) {
        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(command.noteId(), command.userId());
        
        if (noteOptional.isEmpty()) {
            return Optional.empty();
        }
        
        Note note = noteOptional.get();
        note.archive();
        Note savedNote = noteRepository.save(note);
        return Optional.of(savedNote);
    }

    @Override
    @Transactional
    public Optional<Note> handle(UnarchiveNoteCommand command) {
        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(command.noteId(), command.userId());
        
        if (noteOptional.isEmpty()) {
            return Optional.empty();
        }
        
        Note note = noteOptional.get();
        note.unarchive();
        Note savedNote = noteRepository.save(note);
        return Optional.of(savedNote);
    }

    @Override
    @Transactional
    public void handle(DeleteNoteCommand command) {
        noteRepository.deleteByIdAndUserId(command.noteId(), command.userId());
    }
}
