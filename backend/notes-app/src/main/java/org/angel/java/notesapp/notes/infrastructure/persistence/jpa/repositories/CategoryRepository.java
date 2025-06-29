package org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories;

import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    
    void deleteByIdAndUserId(Long id, Long userId);
    
    boolean existsByNameAndUserId(String name, Long userId);
}
