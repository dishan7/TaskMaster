package com.airtribe.taskmaster.controller;

import com.airtribe.taskmaster.dto.ProjectDto;
import com.airtribe.taskmaster.entity.Project;
import com.airtribe.taskmaster.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService _projectService;

    @PostMapping("/createProject")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Project project = _projectService.createProject(username, projectDto);
        return ResponseEntity.status(200).body(project);
    }


}
