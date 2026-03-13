package ru.netology.todo.controller;

import ru.netology.todo.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты REST API для TaskController.
 *
 * Проверяем:
 * - Все CRUD операции (POST, GET, PUT, DELETE)
 * - Коды ответов HTTP (201, 200, 204, 404)
 * - Корректность возвращаемых данных
 * - Валидацию входных данных
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Вспомогательный метод: создаёт задачу через POST и возвращает её id.
     */
    private long createSampleTask(String title) throws Exception {
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Task created = objectMapper.readValue(response, Task.class);
        return created.getId();
    }

    // =================== POST /tasks ===================

    @Test
    void createTask_shouldReturn201AndTask() throws Exception {
        Task task = new Task();
        task.setTitle("Купить молоко");
        task.setCompleted(false);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Купить молоко"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void createTask_withBlankTitle_shouldReturn400() throws Exception {
        Task task = new Task();
        task.setTitle("");   // пустой title — не пройдёт @NotBlank
        task.setCompleted(false);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }

    // =================== GET /tasks ===================

    @Test
    void getAllTasks_shouldReturn200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =================== PUT /tasks/{id} ===================

    @Test
    void updateTask_shouldReturn200AndUpdatedTask() throws Exception {
        long id = createSampleTask("Старое название");

        Task updated = new Task();
        updated.setTitle("Новое название");
        updated.setCompleted(true);

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Новое название"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void updateTask_notFound_shouldReturn404() throws Exception {
        Task updated = new Task();
        updated.setTitle("Несуществующая");
        updated.setCompleted(false);

        mockMvc.perform(put("/tasks/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    // =================== DELETE /tasks/{id} ===================

    @Test
    void deleteTask_shouldReturn204() throws Exception {
        long id = createSampleTask("Удалить эту задачу");

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/tasks/999999"))
                .andExpect(status().isNotFound());
    }
}
