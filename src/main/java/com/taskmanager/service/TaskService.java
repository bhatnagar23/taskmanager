package com.taskmanager.service;

import com.taskmanager.model.*;
import com.taskmanager.model.Task.*;
import com.taskmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    // ---- CREATE TASK (Admin only) ----
    public Task createTask(Map<String, String> body,
                           String creatorEmail) {

        // Find project
        Long projectId = Long.parseLong(body.get("projectId"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found!"));

        // Check if creator is ADMIN of project
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));

        ProjectMember creatorMember = projectMemberRepository
                .findByProjectAndUser(project, creator)
                .orElseThrow(() ->
                        new RuntimeException("You are not a member!"));

        if (creatorMember.getRole() != ProjectMember.Role.ADMIN) {
            throw new RuntimeException("Only Admin can create tasks!");
        }

        // Find assigned user
        Long assignedToId = Long.parseLong(body.get("assignedTo"));
        User assignedUser = userRepository.findById(assignedToId)
                .orElseThrow(() ->
                        new RuntimeException("Assigned user not found!"));

        // Create task
        Task task = new Task();
        task.setTitle(body.get("title"));
        task.setDescription(body.get("description"));
        task.setDueDate(LocalDate.parse(body.get("dueDate")));
        task.setPriority(Priority.valueOf(
                body.get("priority").toUpperCase()));
        task.setStatus(Status.TODO);
        task.setProject(project);
        task.setAssignedTo(assignedUser);
        task.setCreatedBy(creator);

        return taskRepository.save(task);
    }

    // ---- GET ALL TASKS OF A PROJECT ----
    public List<Task> getProjectTasks(Long projectId,
                                      String userEmail) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found!"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));

        // Check membership
        ProjectMember member = projectMemberRepository
                .findByProjectAndUser(project, user)
                .orElseThrow(() ->
                        new RuntimeException("You are not a member!"));

        // Admin sees ALL tasks, Member sees only assigned tasks
        if (member.getRole() == ProjectMember.Role.ADMIN) {
            return taskRepository.findByProject(project);
        } else {
            return taskRepository.findByProjectAndAssignedTo(
                    project, user);
        }
    }

    // ---- UPDATE TASK STATUS ----
    public Task updateTaskStatus(Long taskId,
                                 String newStatus,
                                 String userEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found!"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));

        // Check if user is assigned to this task OR is admin
        ProjectMember member = projectMemberRepository
                .findByProjectAndUser(task.getProject(), user)
                .orElseThrow(() ->
                        new RuntimeException("You are not a member!"));

        boolean isAdmin = member.getRole() ==
                ProjectMember.Role.ADMIN;
        boolean isAssigned = task.getAssignedTo()
                .getId().equals(user.getId());

        if (!isAdmin && !isAssigned) {
            throw new RuntimeException(
                    "You can only update your own tasks!");
        }

        // Update status
        task.setStatus(Status.valueOf(newStatus.toUpperCase()));
        return taskRepository.save(task);
    }

    // ---- DELETE TASK (Admin only) ----
    public String deleteTask(Long taskId, String userEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found!"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));

        // Only admin can delete
        ProjectMember member = projectMemberRepository
                .findByProjectAndUser(task.getProject(), user)
                .orElseThrow(() ->
                        new RuntimeException("You are not a member!"));

        if (member.getRole() != ProjectMember.Role.ADMIN) {
            throw new RuntimeException(
                    "Only Admin can delete tasks!");
        }

        taskRepository.delete(task);
        return "Task deleted successfully!";
    }

    // ---- GET OVERDUE TASKS ----
    public List<Task> getOverdueTasks(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found!"));

        // Tasks where due date is before today and not DONE
        return taskRepository
                .findByProjectAndDueDateBeforeAndStatusNot(
                        project, LocalDate.now(), Status.DONE);
    }
}