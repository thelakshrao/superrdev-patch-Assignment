package com.internal.tasktracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private static final int MAX_PAGE_SIZE = 100;

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        if (page < 1) {
            return badRequest("page must be 1 or greater");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            return badRequest("pageSize must be between 1 and " + MAX_PAGE_SIZE);
        }

        String query = q == null ? "" : q.trim();
        String searchTerm = "%" + query.toLowerCase() + "%";

        String normalizedStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                normalizedStatus = TaskStatus.valueOf(status.trim().toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                return badRequest("Invalid status. Allowed values: " + Arrays.toString(TaskStatus.values()));
            }
        }

                System.out.println("[TaskController] q=\"" + query + "\" status=" + normalizedStatus
                + " page=" + page + " pageSize=" + pageSize);

        Page<Task> result = taskRepository.searchTasks(
                searchTerm, normalizedStatus, PageRequest.of(page - 1, pageSize));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", result.getContent());
        response.put("total", result.getTotalElements());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> badRequest(String message) {
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}