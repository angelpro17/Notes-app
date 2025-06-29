package org.angel.java.notesapp.notes.domain.model.commands;

public record CreateCategoryCommand(
    String name,
    String color,
    Long userId
) {
}
