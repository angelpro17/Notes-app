package org.angel.java.notesapp.notes.interfaces.rest;

import org.angel.java.notesapp.notes.domain.model.commands.*;
import org.angel.java.notesapp.notes.domain.model.queries.*;
import org.angel.java.notesapp.notes.domain.services.NoteCommandService;
import org.angel.java.notesapp.notes.domain.services.NoteQueryService;
import org.angel.java.notesapp.notes.interfaces.rest.resources.*;
import org.angel.java.notesapp.notes.interfaces.rest.transform.*;
import org.angel.java.notesapp.iam.interfaces.acl.IamContextFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notes", description = "Notes Management Endpoints")
public class NotesController {

    private final NoteCommandService noteCommandService;
    private final NoteQueryService noteQueryService;
    private final IamContextFacade iamContextFacade;

    public NotesController(NoteCommandService noteCommandService,
                           NoteQueryService noteQueryService,
                           IamContextFacade iamContextFacade) {
        this.noteCommandService = noteCommandService;
        this.noteQueryService = noteQueryService;
        this.iamContextFacade = iamContextFacade;
    }

    private Optional<Long> getUserIdFromToken() {
        String username = iamContextFacade.fetchUsernameFromToken();
        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }
        return iamContextFacade.fetchUserIdByUsername(username);
    }

    @PostMapping
    public ResponseEntity<NoteResource> createNote(@RequestBody CreateNoteResource createNoteResource) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var createNoteCommand = CreateNoteCommandFromResourceAssembler.toCommandFromResource(createNoteResource, userId);
        var noteId = noteCommandService.handle(createNoteCommand);

        if (noteId == null || noteId == 0) {
            return ResponseEntity.badRequest().build();
        }

        var getNoteQuery = new GetNoteByIdAndUserIdQuery(noteId, userId);
        var note = noteQueryService.handle(getNoteQuery);

        if (note.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var noteResource = NoteResourceFromEntityAssembler.toResourceFromEntity(note.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResource);
    }

    @GetMapping
    public ResponseEntity<List<NoteResource>> getAllNotes() {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getAllNotesQuery = new GetAllNotesByUserIdQuery(userId);
        var notes = noteQueryService.handle(getAllNotesQuery);
        var noteResources = notes.stream()
                .map(NoteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(noteResources);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResource> getNoteById(@PathVariable Long noteId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getNoteQuery = new GetNoteByIdAndUserIdQuery(noteId, userId);
        var note = noteQueryService.handle(getNoteQuery);

        if (note.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var noteResource = NoteResourceFromEntityAssembler.toResourceFromEntity(note.get());
        return ResponseEntity.ok(noteResource);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResource> updateNote(@PathVariable Long noteId, @RequestBody UpdateNoteResource updateNoteResource) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var updateNoteCommand = UpdateNoteCommandFromResourceAssembler.toCommandFromResource(updateNoteResource, noteId, userId);
        var updatedNote = noteCommandService.handle(updateNoteCommand);

        if (updatedNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var noteResource = NoteResourceFromEntityAssembler.toResourceFromEntity(updatedNote.get());
        return ResponseEntity.ok(noteResource);
    }

    @PatchMapping("/{noteId}/archive")
    public ResponseEntity<NoteResource> archiveNote(@PathVariable Long noteId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var archiveNoteCommand = new ArchiveNoteCommand(noteId, userId);
        var archivedNote = noteCommandService.handle(archiveNoteCommand);

        if (archivedNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var noteResource = NoteResourceFromEntityAssembler.toResourceFromEntity(archivedNote.get());
        return ResponseEntity.ok(noteResource);
    }

    @PatchMapping("/{noteId}/unarchive")
    public ResponseEntity<NoteResource> unarchiveNote(@PathVariable Long noteId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var unarchiveNoteCommand = new UnarchiveNoteCommand(noteId, userId);
        var unarchivedNote = noteCommandService.handle(unarchiveNoteCommand);

        if (unarchivedNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var noteResource = NoteResourceFromEntityAssembler.toResourceFromEntity(unarchivedNote.get());
        return ResponseEntity.ok(noteResource);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var deleteNoteCommand = new DeleteNoteCommand(noteId, userId);
        noteCommandService.handle(deleteNoteCommand);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<NoteResource>> getActiveNotes() {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getActiveNotesQuery = new GetActiveNotesByUserIdQuery(userId);
        var notes = noteQueryService.handle(getActiveNotesQuery);
        var noteResources = notes.stream()
                .map(NoteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(noteResources);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<NoteResource>> getArchivedNotes() {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getArchivedNotesQuery = new GetArchivedNotesByUserIdQuery(userId);
        var notes = noteQueryService.handle(getArchivedNotesQuery);
        var noteResources = notes.stream()
                .map(NoteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(noteResources);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<NoteResource>> getNotesByCategory(@PathVariable Long categoryId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getNotesByCategoryQuery = new GetNotesByCategoryAndUserIdQuery(categoryId, userId);
        var notes = noteQueryService.handle(getNotesByCategoryQuery);
        var noteResources = notes.stream()
                .map(NoteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(noteResources);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResource>> searchNotes(@RequestParam String q) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var searchNotesQuery = new SearchNotesByUserIdQuery(q, userId);
        var notes = noteQueryService.handle(searchNotesQuery);
        var noteResources = notes.stream()
                .map(NoteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(noteResources);
    }
}
