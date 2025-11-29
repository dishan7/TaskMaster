package com.airtribe.taskmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String taskSubject;

    private String taskDescription;

    private Long assignedTo;

    private Long projectId;
}
