package com.airtribe.taskmaster.service;


import com.airtribe.taskmaster.dto.ProjectDto;
import com.airtribe.taskmaster.entity.Project;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.exceptions.UserNotFoundException;
import com.airtribe.taskmaster.repository.ProjectRepository;
import com.airtribe.taskmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository _projectRepository;

    @Autowired
    private UserRepository _userRepository;

    public Project createProject(String username, ProjectDto projectDto){
        Project project = new Project();
        User signedInUser = _userRepository.findByUsername(username).orElse(null);
        if(signedInUser == null) throw new UserNotFoundException("User not found!");
        project.setCreatedBy(signedInUser);
        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setMembers(new ArrayList<>());
        project.setTasks(new ArrayList<>());
        return _projectRepository.save(project);
    }
}
