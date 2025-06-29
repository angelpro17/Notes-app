package org.angel.java.notesapp.notes.infrastructure.persistence.jpa.repositories;

import org.angel.java.notesapp.notes.domain.model.aggregates.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    List<Note> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Optional<Note> findByIdAndUserId(Long id, Long userId);
    
    List<Note> findByUserIdAndArchivedOrderByCreatedAtDesc(Long userId, Boolean archived);
    
    @Query("SELECT n FROM Note n JOIN n.categories c WHERE c.id = :categoryId AND n.userId = :userId ORDER BY n.createdAt DESC")
    List<Note> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    
    @Query("SELECT n FROM Note n WHERE n.userId = :userId AND (LOWER(n.title.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.content.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) ORDER BY n.createdAt DESC")
    List<Note> findByUserIdAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
    
    void deleteByIdAndUserId(Long id, Long userId);
}
