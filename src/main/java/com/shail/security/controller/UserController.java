package com.shail.security.controller;

import com.shail.security.model.LoginResponse;
import com.shail.security.model.UserDto;
import com.shail.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
/*
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<String>(userService.registerUser(userDto), HttpStatus.CREATED);
    }
*/
    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<LoginResponse>(userService.login(userDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/admin/message")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAdminMessage(Authentication auth) {
        return new ResponseEntity<String>("Hello, "+auth.getPrincipal()+". This is a welcome message for ADMIN.", HttpStatus.OK);
    }

    @GetMapping(value = "/user/message")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> getUserMessage(Authentication auth) {
        return new ResponseEntity<String>("Hello, "+auth.getPrincipal()+". This is a welcome message for USER.", HttpStatus.OK);
    }

    @GetMapping(value = "/any/message")
    public ResponseEntity<String> getAnyMessage(Authentication auth) {
        return new ResponseEntity<String>("Hello, "+auth.getPrincipal()+". This is a welcome message.", HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody LoginResponse response) {
        try {
            return new ResponseEntity<LoginResponse>(userService.refreshToken(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
        }
    }

}
