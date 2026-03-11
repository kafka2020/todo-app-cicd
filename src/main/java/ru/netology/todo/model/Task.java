package ru.netology.todo.model;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Модель задачи (TODO).
 * Хранится в памяти (без БД), как указано в задании.
 */
public class Task {

    private Long id;

    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    private boolean completed;

    private LocalDate createdAt;

    public Task() {
    }

    public Task(Long id, String title, boolean completed, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    // --- Геттеры и сеттеры ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
