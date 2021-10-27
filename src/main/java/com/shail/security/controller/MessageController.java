package com.shail.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class MessageController {

	@GetMapping(value = "/admin/message")
	public String adminMessage(Authentication auth) {
		String role = "";
		for (GrantedAuthority gauth: auth.getAuthorities()) {
				role = gauth.getAuthority();
		}
		return "<h1>Hello, "+ auth.getName()+ " you are "+ role+"</h1>";
	}
	
	@GetMapping(value = "/user/message")
	public String userMessage(Authentication auth) {
		String role = "";
		for (GrantedAuthority gauth: auth.getAuthorities()) {
				role = gauth.getAuthority();
		}
		return "<h1>Hello, "+ auth.getName()+ " you are "+ role+"</h1>";
	}
	
	@GetMapping(value = "/any/message")
	public String anyMessage(Authentication auth) {
		String role = "";
		for (GrantedAuthority gauth: auth.getAuthorities()) {
				role = gauth.getAuthority();
		}
		return "<h1>Hello, "+ auth.getName()+ " you are "+ role+"</h1>";
	}
}
