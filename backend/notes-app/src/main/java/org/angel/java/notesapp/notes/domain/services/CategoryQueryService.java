package org.angel.java.notesapp.notes.domain.services;

import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface CategoryQueryService {
    List<Category> handle(GetAllCategoriesByUserIdQuery query);
    Optional<Category> handle(GetCategoryByIdAndUserIdQuery query);
}
