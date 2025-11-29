package com.airtribe.taskmaster.controller;

import com.airtribe.taskmaster.dto.CommentDto;
import com.airtribe.taskmaster.entity.Comment;
import com.airtribe.taskmaster.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    private CommentService _commentService;

    @PostMapping("/addComment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(@RequestParam(name = "taskId") Long taskId, CommentDto commentDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Comment comment = _commentService.addComment(username, taskId, commentDto);
        return ResponseEntity.status(200).body(comment);
    }
}
