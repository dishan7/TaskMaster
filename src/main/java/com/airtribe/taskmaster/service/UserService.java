package com.airtribe.taskmaster.service;

import com.airtribe.taskmaster.dto.UserDto;
import com.airtribe.taskmaster.entity.User;
import com.airtribe.taskmaster.entity.VerificationToken;
import com.airtribe.taskmaster.enums.ROLES;
import com.airtribe.taskmaster.exceptions.UserNotFoundException;
import com.airtribe.taskmaster.repository.UserRepository;
import com.airtribe.taskmaster.repository.VerificationTokenRepository;
import com.airtribe.taskmaster.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private VerificationTokenRepository _verificationTokenRepository;

    public User registerUser(UserDto userDto){
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(_passwordEncoder.encode(userDto.getPassword()));
        newUser.setPhoneNumber(userDto.getPhoneNumber());
        newUser.setRole(ROLES.USER);
        newUser.setEnabled(false);
        newUser.setAssignedTasks(new ArrayList<>());
        newUser.setTasksCreated(new ArrayList<>());
        return _userRepository.save(newUser);
    }

    public void saveVerificationToken(String verificationTokenString, User user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(verificationTokenString);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 24));
        _verificationTokenRepository.save(verificationToken);
    }

    public boolean verifyRegistrationToken(String verificationTokenString){
        VerificationToken savedToken =  _verificationTokenRepository.findByToken(verificationTokenString);
        return savedToken != null && savedToken.getExpiryDate().getTime() >= System.currentTimeMillis();
    }

    @Transactional
    public void enableUser(String verificationTokenString){
        VerificationToken savedToken =  _verificationTokenRepository.findByToken(verificationTokenString);
        User user = savedToken.getUser();
        if(user == null){
            throw new UserNotFoundException("User not found");
        }
        user.setEnabled(true);
        _userRepository.save(user);
        _verificationTokenRepository.delete(savedToken);
    }

    public String signInUser(String username, String password){
        User fetchedUser = _userRepository.findByUsername(username).orElse(null);
        if(fetchedUser == null){
            return "User not found!";
        }
        if(!fetchedUser.isEnabled()){
            return "User " + username + " is not verified!";
        }
        boolean passwordMatch = _passwordEncoder.matches(password, fetchedUser.getPassword());
        if(!passwordMatch){
            return "Wrong password, try again!";
        }
        return TokenUtil.generateToken(fetchedUser, fetchedUser.getRole().name());
    }

    public User updateProfile(String username, UserDto userDto){
        User savedUser = _userRepository.findByUsername(username).orElse(null);
        if(savedUser == null){
            throw new UserNotFoundException("User not found!");
        }
        savedUser.setUsername(userDto.getUsername());
        savedUser.setPhoneNumber(userDto.getPhoneNumber());
        return _userRepository.save(savedUser);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
}