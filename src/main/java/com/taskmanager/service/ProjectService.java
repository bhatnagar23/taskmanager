package com.taskmanager.service;

import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.model.ProjectMember.Role;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectMemberRepository;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    // ---- CREATE PROJECT ----
    public Project createProject(String name, String description,
                                 String userEmail) {

        // Find the user who is creating the project
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Create and save the project
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setCreatedBy(user);
        projectRepository.save(project);

        // Automatically add creator as ADMIN in project_members
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRole(Role.ADMIN);
        projectMemberRepository.save(member);

        return project;
    }

    // ---- GET MY PROJECTS ----
    public List<ProjectMember> getMyProjects(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Get all projects where user is a member
        return projectMemberRepository.findByUser(user);
    }

    // ---- ADD MEMBER TO PROJECT ----
    public String addMember(Long projectId, Long userId,
                            String adminEmail) {

        // Find project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        // Check if the person adding is an ADMIN
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ProjectMember adminMember = projectMemberRepository
                .findByProjectAndUser(project, admin)
                .orElseThrow(() -> new RuntimeException("You are not a member!"));

        if (adminMember.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only Admin can add members!");
        }

        // Find user to add
        User userToAdd = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if already a member
        if (projectMemberRepository.existsByProjectAndUser(project, userToAdd)) {
            throw new RuntimeException("User is already a member!");
        }

        // Add as MEMBER
        ProjectMember newMember = new ProjectMember();
        newMember.setProject(project);
        newMember.setUser(userToAdd);
        newMember.setRole(Role.MEMBER);
        projectMemberRepository.save(newMember);

        return "Member added successfully!";
    }

    // ---- REMOVE MEMBER FROM PROJECT ----
    public String removeMember(Long projectId, Long userId,
                               String adminEmail) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        // Check if requester is ADMIN
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ProjectMember adminMember = projectMemberRepository
                .findByProjectAndUser(project, admin)
                .orElseThrow(() -> new RuntimeException("You are not a member!"));

        if (adminMember.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only Admin can remove members!");
        }

        // Find and remove the member
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ProjectMember memberToRemove = projectMemberRepository
                .findByProjectAndUser(project, userToRemove)
                .orElseThrow(() -> new RuntimeException("User is not a member!"));

        projectMemberRepository.delete(memberToRemove);

        return "Member removed successfully!";
    }

    // ---- GET ALL MEMBERS OF A PROJECT ----
    public List<ProjectMember> getProjectMembers(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        return projectMemberRepository.findByProject(project);
    }
}