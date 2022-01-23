package com.shail.security;

import com.shail.security.model.UserEntity;
import com.shail.security.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SecurityApplication {

	@Autowired
	private UserEntityRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@PostConstruct
	public void setup() {
		userRepository.save(new UserEntity(1, "shail@mail.com", passwordEncoder.encode("shail@123"), "ROLE_ADMIN"));
		userRepository.save(new UserEntity(2, "john@mail.com", passwordEncoder.encode("john@123"), "ROLE_USER"));
	}

}
