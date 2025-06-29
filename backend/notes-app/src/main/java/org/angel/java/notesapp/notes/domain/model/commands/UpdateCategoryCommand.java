package org.angel.java.notesapp.notes.domain.model.commands;

public record UpdateCategoryCommand(
    Long categoryId,
    String name,
    String color,
    Long userId
) {
}
