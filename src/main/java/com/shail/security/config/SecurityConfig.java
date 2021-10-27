package com.shail.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		daoAuthProvider.setPasswordEncoder(passwordEncoder());
		daoAuthProvider.setUserDetailsService(userDetailsServiceImpl);
		
		auth.authenticationProvider(daoAuthProvider);

    }
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
			
		http
			.httpBasic().and()
			.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/api/admin/**").hasRole("ADMIN")
			.antMatchers("/api/user/**").hasRole("USER")
			.antMatchers("/api/any/**").hasAnyRole("ADMIN", "USER")
			.anyRequest()
			.authenticated()
			.and().formLogin().disable();
		
		http.csrf().ignoringAntMatchers("/h2-console/**");
		
		http.headers().frameOptions().sameOrigin();
		
		// @formatter:on

	}
	
}
