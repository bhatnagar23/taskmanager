package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // POST /api/tasks → Create task
    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String email = authentication.getName();
        Task task = taskService.createTask(body, email);
        return ResponseEntity.ok(task);
    }

    // GET /api/tasks/project/{projectId} → Get tasks
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getProjectTasks(
            @PathVariable Long projectId,
            Authentication authentication) {

        String email = authentication.getName();
        List<Task> tasks = taskService
                .getProjectTasks(projectId, email);
        return ResponseEntity.ok(tasks);
    }

    // PUT /api/tasks/{id}/status → Update task status
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String email = authentication.getName();
        String newStatus = body.get("status");
        Task task = taskService
                .updateTaskStatus(id, newStatus, email);
        return ResponseEntity.ok(task);
    }

    // DELETE /api/tasks/{id} → Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        String result = taskService.deleteTask(id, email);
        return ResponseEntity.ok(result);
    }

    // GET /api/tasks/project/{projectId}/overdue → Overdue tasks
    @GetMapping("/project/{projectId}/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks(
            @PathVariable Long projectId) {

        List<Task> tasks = taskService.getOverdueTasks(projectId);
        return ResponseEntity.ok(tasks);
    }
}