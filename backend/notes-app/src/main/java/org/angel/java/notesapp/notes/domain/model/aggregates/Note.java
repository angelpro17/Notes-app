package org.angel.java.notesapp.notes.domain.model.aggregates;

import org.angel.java.notesapp.notes.domain.model.entities.Category;
import org.angel.java.notesapp.notes.domain.model.valueobjects.NoteContent;
import org.angel.java.notesapp.notes.domain.model.valueobjects.NoteTitle;
import org.angel.java.notesapp.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Note extends AuditableAbstractAggregateRoot<Note> {

    @Embedded
    private NoteTitle title;

    @Embedded
    private NoteContent content;

    @NotNull
    @Column(name = "archived", nullable = false)
    private Boolean archived = false;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "note_categories",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Note() {
        super();
    }

    public Note(String title, String content, Long userId) {
        this();
        this.title = new NoteTitle(title);
        this.content = new NoteContent(content);
        this.userId = userId;
        this.archived = false;
    }

    public Note(String title, String content, Long userId, Set<Category> categories) {
        this(title, content, userId);
        this.categories = categories != null ? categories : new HashSet<>();
    }

    public void updateTitle(String title) {
        this.title = new NoteTitle(title);
    }

    public void updateContent(String content) {
        this.content = new NoteContent(content);
    }

    public void archive() {
        this.archived = true;
    }

    public void unarchive() {
        this.archived = false;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public void updateCategories(Set<Category> categories) {
        this.categories.clear();
        if (categories != null) {
            this.categories.addAll(categories);
        }
    }

    public String getTitleValue() {
        return this.title != null ? this.title.title() : null;
    }

    public String getContentValue() {
        return this.content != null ? this.content.content() : null;
    }
}
