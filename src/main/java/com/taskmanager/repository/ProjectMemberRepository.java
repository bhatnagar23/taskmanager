package com.taskmanager.repository;

import com.taskmanager.model.Project;
import com.taskmanager.model.ProjectMember;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, Long> {

    // Find all projects a user is member of
    List<ProjectMember> findByUser(User user);

    // Find all members of a project
    List<ProjectMember> findByProject(Project project);

    // Find specific member in specific project
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);

    // Check if user is already a member
    boolean existsByProjectAndUser(Project project, User user);
}