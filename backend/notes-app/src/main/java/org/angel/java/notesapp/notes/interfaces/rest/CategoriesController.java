package org.angel.java.notesapp.notes.interfaces.rest;

import org.angel.java.notesapp.notes.domain.model.commands.*;
import org.angel.java.notesapp.notes.domain.model.queries.*;
import org.angel.java.notesapp.notes.domain.services.CategoryCommandService;
import org.angel.java.notesapp.notes.domain.services.CategoryQueryService;
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
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Categories Management Endpoints")
public class CategoriesController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;
    private final IamContextFacade iamContextFacade;

    public CategoriesController(CategoryCommandService categoryCommandService,
                                CategoryQueryService categoryQueryService,
                                IamContextFacade iamContextFacade) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
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
    public ResponseEntity<CategoryResource> createCategory(@RequestBody CreateCategoryResource createCategoryResource) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var createCategoryCommand = CreateCategoryCommandFromResourceAssembler.toCommandFromResource(createCategoryResource, userId);
        var categoryId = categoryCommandService.handle(createCategoryCommand);

        if (categoryId == null || categoryId == 0) {
            return ResponseEntity.badRequest().build();
        }

        var getCategoryQuery = new GetCategoryByIdAndUserIdQuery(categoryId, userId);
        var category = categoryQueryService.handle(getCategoryQuery);

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var categoryResource = CategoryResourceFromEntityAssembler.toResourceFromEntity(category.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResource);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResource>> getAllCategories() {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getAllCategoriesQuery = new GetAllCategoriesByUserIdQuery(userId);
        var categories = categoryQueryService.handle(getAllCategoriesQuery);
        var categoryResources = categories.stream()
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(categoryResources);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResource> getCategoryById(@PathVariable Long categoryId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var getCategoryQuery = new GetCategoryByIdAndUserIdQuery(categoryId, userId);
        var category = categoryQueryService.handle(getCategoryQuery);

        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var categoryResource = CategoryResourceFromEntityAssembler.toResourceFromEntity(category.get());
        return ResponseEntity.ok(categoryResource);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResource> updateCategory(@PathVariable Long categoryId, @RequestBody UpdateCategoryResource updateCategoryResource) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var updateCategoryCommand = UpdateCategoryCommandFromResourceAssembler.toCommandFromResource(updateCategoryResource, categoryId, userId);
        var updatedCategory = categoryCommandService.handle(updateCategoryCommand);

        if (updatedCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var categoryResource = CategoryResourceFromEntityAssembler.toResourceFromEntity(updatedCategory.get());
        return ResponseEntity.ok(categoryResource);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        Optional<Long> userIdOptional = getUserIdFromToken();
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userIdOptional.get();

        var deleteCategoryCommand = new DeleteCategoryCommand(categoryId, userId);
        categoryCommandService.handle(deleteCategoryCommand);
        return ResponseEntity.noContent().build();
    }
}
