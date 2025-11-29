package com.airtribe.taskmaster.service;

import com.airtribe.taskmaster.dto.CommentDto;
import com.airtribe.taskmaster.entity.Comment;
import com.airtribe.taskmaster.entity.Task;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.exceptions.TaskNotFoundException;
import com.airtribe.taskmaster.exceptions.UserNotAuthorizedException;
import com.airtribe.taskmaster.exceptions.UserNotFoundException;
import com.airtribe.taskmaster.repository.CommentRepository;
import com.airtribe.taskmaster.repository.TaskRepository;
import com.airtribe.taskmaster.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class CommentService {

    @Autowired
    private CommentRepository _commentRepository;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private TaskRepository _taskRepository;

    @Transactional
    public Comment addComment(String username, Long taskId, CommentDto commentDto){
        User signedInUser = _userRepository.findByUsername(username).orElse(null);
        if(signedInUser == null) throw new UserNotFoundException("User Not Found!");
        Task task = _taskRepository.findById(taskId).orElse(null);
        if(task == null) throw new TaskNotFoundException("Task not found!");

        if(!isUserAuthorized(signedInUser, task)) throw new UserNotAuthorizedException("User not authorized");
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());

        _commentRepository.save(comment);
        task.getCommentsList().add(comment);
        _taskRepository.save(task);

        return comment;
    }

    public boolean isUserAuthorized(User signedInUser, Task task){
        return task.getProject().getMembers().contains(signedInUser);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotAuthorizedException(Exception e){
        return ResponseEntity.status(401).body("User not authorized!");
    }
}
