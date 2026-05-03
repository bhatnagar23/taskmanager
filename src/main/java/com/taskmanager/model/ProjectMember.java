package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which project
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Which user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Their role - ADMIN or MEMBER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Enum for roles
    public enum Role {
        ADMIN, MEMBER
    }
}