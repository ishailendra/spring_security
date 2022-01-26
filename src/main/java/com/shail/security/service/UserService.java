package com.shail.security.service;

import com.shail.security.helper.TokenHelper;
import com.shail.security.model.LoginResponse;
import com.shail.security.model.RefreshTokenEntity;
import com.shail.security.model.UserDto;
import com.shail.security.model.UserEntity;
import com.shail.security.repository.RefreshTokenRepository;
import com.shail.security.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenRepository refreshTokenReository;

    private final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private final String REFRESH_TOKEN = "REFRESH_TOKEN";

/*
    public String registerUser(UserDto userDto) {
        UserEntity entity = new UserEntity();
        entity.setName(userDto.getName());
        entity.setUsername(userDto.getUsername());
        entity.setPassword(userDto.getPassword());
        entity.setRoles("ROLE_ADMIN,ROLE_USER");

        userRepository.save(entity);
        return "USER REGISTERED SUCCESSFULLY!";
    }
*/
    public LoginResponse login(UserDto userDto) throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        LoginResponse response = new LoginResponse();

        if(authenticate.isAuthenticated()) {
            User user = (User) authenticate.getPrincipal();
            tokenHelper.generateToken(user.getUsername(), ACCESS_TOKEN, (Collection<GrantedAuthority>)  authenticate.getAuthorities(), response);
            tokenHelper.generateToken(user.getUsername(), REFRESH_TOKEN, (Collection<GrantedAuthority>)  authenticate.getAuthorities(), response);
            refreshTokenReository.save(new RefreshTokenEntity(response.getRefreshToken(), true));
        }
        else {
            throw new Exception("INVALID USERNAME/PASSWORD");
        }
        return response;
    }

    public LoginResponse refreshToken(LoginResponse response) throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        Optional<RefreshTokenEntity> token = refreshTokenReository.findByToken(response.getRefreshToken());
        boolean isValid = false;
        if (token.isPresent()) {
            RefreshTokenEntity refreshToken = token.get();
            isValid = refreshToken.isValidity();
            refreshToken.setValidity(false);
            refreshTokenReository.save(refreshToken);
        }

        if(tokenHelper.isValidToken(response.getRefreshToken(), response.getRefreshKid()) && isValid) {

            String username = tokenHelper.getUsername(response.getRefreshToken(), response.getRefreshKid());
            UserDetails user = userDetailsService.loadUserByUsername(username);

            tokenHelper.generateToken(user.getUsername(), ACCESS_TOKEN, (Collection<GrantedAuthority>)  user.getAuthorities(), loginResponse);
            tokenHelper.generateToken(user.getUsername(), REFRESH_TOKEN, (Collection<GrantedAuthority>)  user.getAuthorities(), loginResponse);
            refreshTokenReository.save(new RefreshTokenEntity(loginResponse.getRefreshToken(), true));
        }
        else {
            throw new Exception("INVALID_REFRESH_TOKEN");
        }

        return loginResponse;
    }
}
