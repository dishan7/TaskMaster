package com.airtribe.taskmaster.controller;

import com.airtribe.taskmaster.dto.TaskDto;
import com.airtribe.taskmaster.entity.Task;
import com.airtribe.taskmaster.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService _taskService;

    @PostMapping("/createTask")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String createdByUsername = authentication.getName();
        System.out.println(createdByUsername);
        Task createdTask = _taskService.createTask(taskDto, createdByUsername);
        return ResponseEntity.status(200).body(createdTask);
    }

    @GetMapping("/fetchAssignedTasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Task>> fetchAssignedTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Task> assignedTasks = _taskService.fetchAssignedTasks(username);
        return ResponseEntity.status(200).body(assignedTasks);
    }

    @PutMapping("/updateTaskStatus")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> updateTaskStatus(@RequestParam(name = "taskId") Long taskId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Task updatedTask = _taskService.updateTaskStatus(username, taskId);
        return ResponseEntity.status(200).body(updatedTask);
    }

    @PutMapping("/assignTaskToUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> assignTaskToUser(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "taskId") Long taskId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Task savedTask = _taskService.assignTaskToUser(username, userId, taskId);
        return ResponseEntity.status(200).body(savedTask);
    }

    @GetMapping("/fetchTasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Task>> fetchTasks(@RequestParam(name = "status", required = false) String status,
                                                 @RequestParam(name = "subject", required = false) String subject){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(status == null && subject == null){
            List<Task> tasks = _taskService.fetchAssignedTasks(username);
            return ResponseEntity.status(200).body(tasks);
        }
        if(status != null){
            List<Task> tasks = _taskService.fetchAssignedTasksByStatus(username, status);
            return ResponseEntity.status(200).body(tasks);
        }
        List<Task> tasks = _taskService.fetchAssignedTasksByTitle(username, subject);
        return ResponseEntity.status(200).body(tasks);
    }
}
