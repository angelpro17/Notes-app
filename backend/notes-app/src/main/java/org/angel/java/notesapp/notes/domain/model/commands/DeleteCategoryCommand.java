package org.angel.java.notesapp.notes.domain.model.commands;

public record DeleteCategoryCommand(
    Long categoryId,
    Long userId
) {
}
