package com.airtribe.taskmaster.entity;

import com.airtribe.taskmaster.enums.ROLES;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Column(unique = true, nullable = false)
    private String username;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = true)
    private String phoneNumber;

    private String password;

    private boolean isEnabled;

    private ROLES role;

    @OneToMany
    private List<Task> assignedTasks;

    @OneToMany
    private List<Task> tasksCreated;

    @ManyToMany
    private List<Project> projects;

    @OneToMany
    private List<Project> projectsCreated;
}
