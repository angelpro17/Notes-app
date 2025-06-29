package org.angel.java.notesapp.notes.domain.services;

import org.angel.java.notesapp.notes.domain.model.commands.*;
import org.angel.java.notesapp.notes.domain.model.entities.Category;

import java.util.Optional;

public interface CategoryCommandService {
    Long handle(CreateCategoryCommand command);
    Optional<Category> handle(UpdateCategoryCommand command);
    void handle(DeleteCategoryCommand command);
}
