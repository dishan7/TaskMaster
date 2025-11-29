package com.airtribe.taskmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity{

    private String projectName;

    private String projectDescription;

    @ManyToOne
    private User createdBy;

    @ManyToMany
    @JsonIgnore
    private List<User> members;

    @OneToMany
    private List<Task> tasks;
}
