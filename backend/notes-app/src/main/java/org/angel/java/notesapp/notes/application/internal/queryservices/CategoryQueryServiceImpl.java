package org.angel.java.notesapp.notes.application.internal.queryservices;

import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.domain.model.queries.*;
import org.angel.java.notesapp.notes.domain.services.CategoryQueryService;
import org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public CategoryQueryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> handle(GetAllCategoriesByUserIdQuery query) {
        return categoryRepository.findByUserIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public Optional<Category> handle(GetCategoryByIdAndUserIdQuery query) {
        return categoryRepository.findByIdAndUserId(query.categoryId(), query.userId());
    }
}
