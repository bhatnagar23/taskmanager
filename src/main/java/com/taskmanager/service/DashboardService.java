package com.taskmanager.service;

import com.taskmanager.dto.DashboardResponse;
import com.taskmanager.model.Task;
import com.taskmanager.model.Project;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public DashboardResponse getDashboard(Long projectId) {

        // Find project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found!"));

        // Get ALL tasks of this project
        List<Task> allTasks = taskRepository
                .findByProject(project);

        // ---- 1. TOTAL TASKS ----
        long totalTasks = allTasks.size();

        // ---- 2. TASKS BY STATUS ----
        Map<String, Long> tasksByStatus = new HashMap<>();
        tasksByStatus.put("TODO", allTasks.stream()
                .filter(t -> t.getStatus() == Task.Status.TODO)
                .count());
        tasksByStatus.put("IN_PROGRESS", allTasks.stream()
                .filter(t -> t.getStatus() ==
                        Task.Status.IN_PROGRESS)
                .count());
        tasksByStatus.put("DONE", allTasks.stream()
                .filter(t -> t.getStatus() == Task.Status.DONE)
                .count());

        // ---- 3. TASKS PER USER ----
        Map<String, Long> tasksPerUser = new HashMap<>();
        for (Task task : allTasks) {
            if (task.getAssignedTo() != null) {

                // Get user's name
                String userName = task.getAssignedTo().getName();

                // Add 1 to their count
                // If user not in map yet, start from 0
                tasksPerUser.put(userName,
                        tasksPerUser.getOrDefault(userName, 0L) + 1);
            }
        }

        // ---- 4. OVERDUE TASKS ----
        long overdueTasks = allTasks.stream()
                .filter(t -> t.getDueDate() != null
                        && t.getDueDate().isBefore(LocalDate.now())
                        && t.getStatus() != Task.Status.DONE)
                .count();

        // Return all stats together
        return new DashboardResponse(
                totalTasks,
                tasksByStatus,
                tasksPerUser,
                overdueTasks
        );
    }
}