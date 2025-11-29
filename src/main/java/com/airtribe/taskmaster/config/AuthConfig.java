package com.airtribe.taskmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try{
            httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("/registerUser", "/verifyRegistrationToken", "/signin", "/createTask", "/createProject", "/addComment", "/fetchTasks", "/assignTaskToUser", "/updateTaskStatus", "/fetchAssignedTasks", "/updateProfile")
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                    .formLogin(formLogin -> formLogin.defaultSuccessUrl("/", true)
                            .permitAll());

            return httpSecurity.build();
        }
        catch(Exception e){
            throw new RuntimeException("Error configuring security filter chain", e);
        }
    }
}
