package org.angel.java.notesapp.notes.domain.model.entities;

import org.angel.java.notesapp.notes.domain.model.valueobjects.CategoryColor;
import org.angel.java.notesapp.notes.domain.model.valueobjects.CategoryName;
import org.angel.java.notesapp.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CategoryName name;

    @Embedded
    private CategoryColor color;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Category() {
        super();
    }

    public Category(String name, Long userId) {
        this();
        this.name = new CategoryName(name);
        this.userId = userId;
        this.color = new CategoryColor("#3f51b5"); // Default color
    }

    public Category(String name, String color, Long userId) {
        this();
        this.name = new CategoryName(name);
        this.color = new CategoryColor(color);
        this.userId = userId;
    }

    public void updateName(String name) {
        this.name = new CategoryName(name);
    }

    public void updateColor(String color) {
        this.color = new CategoryColor(color);
    }

    public String getNameValue() {
        return this.name != null ? this.name.name() : null;
    }

    public String getColorValue() {
        return this.color != null ? this.color.color() : null;
    }
}
