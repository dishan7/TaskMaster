package com.airtribe.taskmaster.controller;

import com.airtribe.taskmaster.dto.UserDto;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService _userService;

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
        User savedUser = _userService.registerUser(userDto);
        String verificationTokenString = UUID.randomUUID().toString();
        _userService.saveVerificationToken(verificationTokenString, savedUser);
        String verificationUrl = "http://localhost:9010/verifyRegistrationToken?verificationToken=" + verificationTokenString;
        System.out.println(verificationUrl);
        return ResponseEntity.status(200).body(savedUser);
    }

    @GetMapping("/verifyRegistrationToken")
    public ResponseEntity<String> verifyRegistrationToken(@RequestParam(name = "verificationToken") String verificationTokenString){
        boolean isTokenValid = _userService.verifyRegistrationToken(verificationTokenString);
        if(!isTokenValid){
            return  ResponseEntity.status(200).body("Verification url expired");
        }
        _userService.enableUser(verificationTokenString);
        return ResponseEntity.status(200).body("User verification successful");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signInInUser(@RequestParam(name = "username") String username,
                                             @RequestParam(name = "password") String password){
        String response = _userService.signInUser(username, password);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/updateProfile")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<User> updateProfile(@RequestBody UserDto userDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = _userService.updateProfile(username, userDto);
        return ResponseEntity.status(200).body(updatedUser);
    }
}
