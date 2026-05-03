package com.taskmanager.repository;

import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Get all tasks of a project
    List<Task> findByProject(Project project);

    // Get all tasks assigned to a user
    List<Task> findByAssignedTo(User user);

    // Get tasks by status in a project
    List<Task> findByProjectAndStatus(
            Project project, Task.Status status);

    // Get overdue tasks (due date passed + not done)
    List<Task> findByProjectAndDueDateBeforeAndStatusNot(
            Project project, LocalDate date, Task.Status status);

    // Get tasks assigned to user in a project
    List<Task> findByProjectAndAssignedTo(
            Project project, User user);
}