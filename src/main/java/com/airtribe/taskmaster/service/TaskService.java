package com.airtribe.taskmaster.service;

import com.airtribe.taskmaster.dto.TaskDto;
import com.airtribe.taskmaster.entity.Project;
import com.airtribe.taskmaster.entity.Task;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.enums.STATUS;
import com.airtribe.taskmaster.exceptions.ProjectNotFoundException;
import com.airtribe.taskmaster.exceptions.TaskNotFoundException;
import com.airtribe.taskmaster.exceptions.UserNotFoundException;
import com.airtribe.taskmaster.repository.ProjectRepository;
import com.airtribe.taskmaster.repository.TaskRepository;
import com.airtribe.taskmaster.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository _taskRepository;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private ProjectRepository _projectRepository;

    @Transactional
    public Task createTask(TaskDto taskDto, String createdByUsername){
        Task task = new Task();
        task.setTaskSubject(taskDto.getTaskSubject());
        task.setTaskDescription(taskDto.getTaskDescription());

        User createdByUser = _userRepository.findByUsername(createdByUsername).orElse(null);
        if(createdByUser == null){
            throw new UserNotFoundException("created by user not found!");
        }
        User assignedToUser = _userRepository.findById(taskDto.getAssignedTo()).orElse(null);

        if(assignedToUser == null){
            throw new UserNotFoundException("User not found!");
        }

        task.setCreatedBy(createdByUser);
        task.setAssignedTo(assignedToUser);
        task.setCommentsList(new ArrayList<>());
        task.setStatus(STATUS.OPEN);
        Project project = _projectRepository.findById(taskDto.getProjectId()).orElse(null);
        if(project == null) throw new ProjectNotFoundException("Project not found!!");
        project.getTasks().add(task);
        task.setProject(project);
        createdByUser.getTasksCreated().add(task);
        assignedToUser.getAssignedTasks().add(task);
        return _taskRepository.save(task);
    }

    public List<Task> fetchAssignedTasks(String username){
        User savedUser = _userRepository.findByUsername(username).orElse(null);
        if(savedUser == null){
            throw new UserNotFoundException("User not found!");
        }
        return savedUser.getAssignedTasks();
    }

    public Task updateTaskStatus(String username, Long taskId) throws Exception {
        User savedUser = _userRepository.findByUsername(username).orElse(null);
        if(savedUser == null){
            throw new UserNotFoundException("User not found");
        }
        Task savedTask = _taskRepository.findById(taskId).orElse(null);
        if(savedTask == null){
            throw new TaskNotFoundException("Task not found");
        }
        if(savedTask.getAssignedTo() != savedUser){
            throw new Exception("User not authorized");
        }
        savedTask.setStatus(STATUS.COMPLETED);
        return _taskRepository.save(savedTask);
    }

    public Task assignTaskToUser(String username, Long userId, Long taskId) throws Exception {
        Task savedTask = _taskRepository.findById(taskId).orElse(null);
        if(savedTask == null) throw new TaskNotFoundException("Task not found!!");

        User savedUser = _userRepository.findByUsername(username).orElse(null);
        if(savedUser == null) throw new UserNotFoundException("No user found with username: " + username);

        User assignToUser = _userRepository.findById(userId).orElse(null);
        if(assignToUser == null) throw new UserNotFoundException("No user found with id: " + userId);

        if(savedTask.getCreatedBy() != savedUser || savedTask.getAssignedTo() != savedUser){
            throw new Exception("Unauthorized user");
        }

        savedTask.setAssignedTo(assignToUser);
        return _taskRepository.save(savedTask);
    }

    public List<Task> fetchAssignedTasksByStatus(String username, String status){
        User savedUser = _userRepository.findByUsername(username).orElse(null);

        if(savedUser == null) throw new UserNotFoundException("User not found!");

        return savedUser.getAssignedTasks()
                .stream()
                .filter(task -> task.getStatus().name().equals(status))
                .toList();
    }

    public List<Task> fetchAssignedTasksByTitle(String username, String subject){
        User savedUser = _userRepository.findByUsername(username).orElse(null);

        if(savedUser == null) throw new UserNotFoundException("User not found!");

        return savedUser.getAssignedTasks()
                .stream()
                .filter(task -> task.getTaskSubject().equals(subject))
                .toList();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleProjectNotFoundException(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
