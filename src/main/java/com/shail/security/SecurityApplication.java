package com.shail.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.shail.security.model.UserEntity;
import com.shail.security.repository.UserRepository;

@SpringBootApplication
public class SecurityApplication {

	@Autowired
	private UserRepository userRepositoty;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	
	@PostConstruct
	public void setup() {
		userRepositoty.save(new UserEntity(1, "shail@mail.com", passwordEncoder.encode("shail@123"), "ROLE_ADMIN"));
		userRepositoty.save(new UserEntity(2, "john@mail.com", passwordEncoder.encode("john@123"), "ROLE_USER"));
	}
}
