package com.shail.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		
        auth.inMemoryAuthentication()
        		.withUser("shail")
        		.password("{noop}shail123")
        		.roles("ADMIN");
        
        auth.inMemoryAuthentication()
        		.withUser("john")
        		.password("{noop}john123")
        		.roles("USER");
        
     // @formatter:on
    }
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
			
		http
			.authorizeRequests()
			.antMatchers("/api/admin/**").hasRole("ADMIN")
			.antMatchers("/api/user/**").hasRole("USER")
			.antMatchers("/api/any/**").hasAnyRole("ADMIN", "USER")
			.anyRequest()
			.authenticated()
			.and().formLogin();
		
		// @formatter:on

	}
	
}
