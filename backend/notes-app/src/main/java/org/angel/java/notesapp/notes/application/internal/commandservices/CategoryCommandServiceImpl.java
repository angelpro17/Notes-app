package org.angel.java.notesapp.notes.application.internal.commandservices;

import org.angel.java.notesapp.notes.domain.model.commands.*;
import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.domain.services.CategoryCommandService;
import org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public CategoryCommandServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Long handle(CreateCategoryCommand command) {
        Category category = new Category(command.name(), command.color(), command.userId());
        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getId();
    }

    @Override
    @Transactional
    public Optional<Category> handle(UpdateCategoryCommand command) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUserId(command.categoryId(), command.userId());
        
        if (categoryOptional.isEmpty()) {
            return Optional.empty();
        }
        
        Category category = categoryOptional.get();
        category.updateName(command.name());
        if (command.color() != null) {
            category.updateColor(command.color());
        }
        
        Category savedCategory = categoryRepository.save(category);
        return Optional.of(savedCategory);
    }

    @Override
    @Transactional
    public void handle(DeleteCategoryCommand command) {
        categoryRepository.deleteByIdAndUserId(command.categoryId(), command.userId());
    }
}
