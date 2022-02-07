package com.shail.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(passwordEncoder::encode);
        List<GrantedAuthority> adminAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        List<GrantedAuthority> userAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        UserDetails adminUser = userBuilder.username("shail@mail.com").password("shail@123").authorities(adminAuthorities).build();
        UserDetails userUser = userBuilder.username("john@mail.com").password("john@123").authorities(userAuthorities).build();

        return new InMemoryUserDetailsManager(Arrays.asList(adminUser, userUser));
    }

}
