package com.shail.security.controller;

import com.shail.security.helper.TokenHelper;
import com.shail.security.model.UserDto;
import com.shail.security.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "api")
public class MessageController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserEntityRepository repository;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        if(auth != null) {
            for (GrantedAuthority gauth : auth.getAuthorities()) {
                role = gauth.getAuthority();
            }
            return "<h1>Hello, " + auth.getName() + " you are " + role + "</h1>";
        }
        return "<h1>Hello, unknown user, this url is public to access</h1>";
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public ResponseEntity<String> validateLogin(@RequestBody UserDto user) throws Exception{
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authenticate.isAuthenticated()) {
            User user1 = (User) authenticate.getPrincipal();
            return new ResponseEntity<String>(tokenHelper.generateToken( user1.getUsername(), (Collection<GrantedAuthority>) authenticate.getAuthorities()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("INVALID USERNAME/PASSWORD", HttpStatus.BAD_REQUEST);
        }
    }
}