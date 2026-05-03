package com.taskmanager.controller;

import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // POST /api/projects → Create project
    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        // Authentication gives us the logged in user's email
        String email = authentication.getName();
        String name = body.get("name");
        String description = body.get("description");

        Project project = projectService.createProject(
                name, description, email);
        return ResponseEntity.ok(project);
    }

    // GET /api/projects → Get my projects
    @GetMapping
    public ResponseEntity<List<ProjectMember>> getMyProjects(
            Authentication authentication) {

        String email = authentication.getName();
        List<ProjectMember> projects =
                projectService.getMyProjects(email);
        return ResponseEntity.ok(projects);
    }

    // POST /api/projects/{id}/members → Add member
    @PostMapping("/{id}/members")
    public ResponseEntity<String> addMember(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body,
            Authentication authentication) {

        String email = authentication.getName();
        Long userId = body.get("userId");
        String result = projectService.addMember(id, userId, email);
        return ResponseEntity.ok(result);
    }

    // DELETE /api/projects/{id}/members/{userId} → Remove member
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();
        String result = projectService.removeMember(id, userId, email);
        return ResponseEntity.ok(result);
    }

    // GET /api/projects/{id}/members → Get all members
    @GetMapping("/{id}/members")
    public ResponseEntity<List<ProjectMember>> getMembers(
            @PathVariable Long id) {

        List<ProjectMember> members =
                projectService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }
}