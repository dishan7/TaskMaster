package com.airtribe.taskmaster;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskmasterApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TaskmasterApplication.class, args);

        Environment env = context.getEnvironment();
        String serverPort = env.getProperty("server.port");

        System.out.println("Application is running on port:" + serverPort);
	}

}
