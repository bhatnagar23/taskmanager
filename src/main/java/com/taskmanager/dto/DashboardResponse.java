package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // Total number of tasks in project
    private long totalTasks;

    // Tasks count by status
    // Example: {"TODO": 3, "IN_PROGRESS": 2, "DONE": 5}
    private Map<String, Long> tasksByStatus;

    // Tasks count per user
    // Example: {"John": 4, "Jane": 3}
    private Map<String, Long> tasksPerUser;

    // Number of overdue tasks
    private long overdueTasks;
}