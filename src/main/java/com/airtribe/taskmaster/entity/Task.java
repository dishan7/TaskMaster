package com.airtribe.taskmaster.entity;

import com.airtribe.taskmaster.enums.STATUS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comments;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity{

    private String taskSubject;

    private String taskDescription;

    @ManyToOne
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JsonIgnore
    private User assignedTo;

    @OneToMany
    private List<Comment> commentsList;

    private STATUS status;

    @ManyToOne
    @JsonIgnore
    private Project project;
}
