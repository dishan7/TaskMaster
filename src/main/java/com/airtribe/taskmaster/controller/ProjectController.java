package com.airtribe.taskmaster.controller;

import com.airtribe.taskmaster.dto.ProjectDto;
import com.airtribe.taskmaster.entity.Project;
import com.airtribe.taskmaster.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/inviteUserToProject")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> inviteUserToProject(@RequestParam(name = "projectId") Long projectId,
                                                       @RequestParam(name = "userId") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String verificationTokenString = UUID.randomUUID().toString();
        _projectService.inviteUserToProject(userId, verificationTokenString);
        String verificationUrl = "http://localhost:9010/acceptInvite?projectId=" + projectId + "&verificationToken=" + verificationTokenString;
        System.out.println(verificationUrl);
        return ResponseEntity.status(200).body("Invite Sent");
    }

    @PutMapping("/acceptInvite")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> acceptInvite(@RequestParam(name = "projectId") Long projectId,
                                                @RequestParam(name = "verificationToken") String verificationToken) throws Exception {
        Project project = _projectService.acceptInvite(projectId, verificationToken);
        return ResponseEntity.status(200).body(project);
    }
}
