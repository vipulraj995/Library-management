package com.vraj.library;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vraj.library.entity.User;
import com.vraj.library.service.UserServiceImpl;

@SpringBootApplication
public class LibraryApplication {

	@Autowired
	private UserServiceImpl userService;
	
	@PostConstruct
	public void initUsers() {
		User user1 = User.builder()
				.scardId("ABC")
				.username("vip@gmail.com")
				.password("pass1")
				.firstname("vipul")
				.lastname("raj")
				.phone("9552262498")
				.college("SCOE")
				.active(true)
				.build();
		User user2 = User.builder()
				.scardId("DEF")
				.username("aish@gmail.com")
				.password("pass2")
				.firstname("Aishwarya")
				.lastname("Surwase")
				.phone("998767342")
				.college("SCOE")
				.active(true)
				.build();
		userService.save(user1, "ROLE_ADMIN");
		userService.save(user2, "ROLE_USER");

	}
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
