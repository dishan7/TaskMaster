package com.airtribe.taskmaster.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;

    @Email
    private String email;

    private String password;

    private String phoneNumber;

    public UserDto(String username, @Email String email, String phoneNumber) {
    }
}
