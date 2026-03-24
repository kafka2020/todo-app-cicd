package ru.netology.todo.controller;

import ru.netology.todo.model.Task;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * REST-контроллер для управления задачами (TODO-лист).
 *
 * Эндпоинты:
 *   POST   /tasks       — добавить задачу
 *   GET    /tasks       — получить все задачи
 *   PUT    /tasks/{id}  — обновить задачу
 *   DELETE /tasks/{id}  — удалить задачу
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();

    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * POST /tasks — создать новую задачу.
     * @param task тело запроса с полями title, completed
     * @return созданная задача с присвоенным id и датой создания
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        task.setId(idCounter.getAndIncrement());
        task.setCreatedAt(LocalDate.now());
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    /**
     * GET /tasks — получить список всех задач.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks);
    }

    /**
     * PUT /tasks/{id} — обновить существующую задачу.
     * @param id идентификатор задачи
     * @param updatedTask новые данные задачи
     * @return обновлённая задача или 404, если не найдена
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @Valid @RequestBody Task updatedTask) {
        Optional<Task> existing = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = existing.get();
        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        return ResponseEntity.ok(task);
    }

    /**
     * DELETE /tasks/{id} — удалить задачу по id.
     * @param id идентификатор задачи
     * @return 204 No Content при успехе, 404 если не найдена
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
