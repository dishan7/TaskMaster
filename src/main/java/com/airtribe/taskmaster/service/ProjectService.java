package com.airtribe.taskmaster.service;


import com.airtribe.taskmaster.dto.ProjectDto;
import com.airtribe.taskmaster.entity.Project;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.entity.VerificationToken;
import com.airtribe.taskmaster.exceptions.UserNotFoundException;
import com.airtribe.taskmaster.repository.ProjectRepository;
import com.airtribe.taskmaster.repository.UserRepository;
import com.airtribe.taskmaster.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository _projectRepository;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private VerificationTokenRepository _verificationTokenRepository;

    public Project createProject(String username, ProjectDto projectDto){
        Project project = new Project();
        User signedInUser = _userRepository.findByUsername(username).orElse(null);
        if(signedInUser == null) throw new UserNotFoundException("User not found!");
        project.setCreatedBy(signedInUser);
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setMembers(new ArrayList<>());
        project.getMembers().add(signedInUser);
        project.setTasks(new ArrayList<>());
        return _projectRepository.save(project);
    }

    public void inviteUserToProject(Long userId, String verificationTokenString){
        User signedInUser = _userRepository.findById(userId).orElse(null);
        if(signedInUser == null) throw new UserNotFoundException("User not found!");
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(signedInUser);
        verificationToken.setToken(verificationTokenString);
        _verificationTokenRepository.save(verificationToken);
    }

    public Project acceptInvite(Long projectId, String verificationTokenString) throws Exception {
        Project project = _projectRepository.findById(projectId).orElse(null);
        if(project == null) throw new Exception("Project not found with id " + projectId);
        VerificationToken verificationToken = _verificationTokenRepository.findByToken(verificationTokenString);
        project.getMembers().add(verificationToken.getUser());
        return _projectRepository.save(project);
    }
}
