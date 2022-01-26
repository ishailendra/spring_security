package com.shail.security;

import com.shail.security.model.UserEntity;
import com.shail.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@PostConstruct
	public void setup() {
		userRepository.save(new UserEntity(1, "shail@mail.com", passwordEncoder.encode("shail@123"), "Shail", "ROLE_ADMIN"));
		userRepository.save(new UserEntity(2, "john@mail.com", passwordEncoder.encode("john@123"), "John", "ROLE_USER"));
		userRepository.save(new UserEntity(3, "jack@mail.com", passwordEncoder.encode("jack@123"), "Jack", "ROLE_ADMIN,ROLE_USER"));
		userRepository.save(new UserEntity(4, "emma@mail.com", passwordEncoder.encode("emma@123"), "Emma", "ROLE_USER"));
	}
}
