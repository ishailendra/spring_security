package com.shail.security.filter;

//import com.shail.record.exceptionHandler.InvalidJwtTokenException;
//import com.shail.record.model.TokenHolder;
//import com.shail.record.security.helper.AccessTokenHelper;
//import com.shail.record.security.service.TokenBasedAuthentication;
//import com.shail.record.security.service.UserDetailServiceImpl;
//import com.shail.record.util.AppConstant;
//import com.shail.record.util.GlobalResponseUtil;
import com.shail.security.helper.TokenHelper;
import com.shail.security.service.UserDetailsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private TokenHelper tokenHelper;
	
	private final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private final String REFRESH_TOKEN = "REFRESH_TOKEN";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = tokenHelper.getToken(request);
		Integer kid = tokenHelper.getKid(request);

		if(!Objects.isNull(token) && !Objects.isNull(kid)) {
			try {
				String scope = tokenHelper.getTokenScope(token, kid);
				String requestUri = request.getRequestURI();

				if (ACCESS_TOKEN.equals(scope) || (REFRESH_TOKEN.equals(scope) && requestUri.contains("refreshToken"))) {
					String username = tokenHelper.getUsername(token, kid);

					if(!Objects.isNull(username) && tokenHelper.isValidToken(token, kid)) {
						UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
					else {
						throw new Exception("INVALID_USERNAME");
					}
				}
				else {
					throw new Exception("INCORRECT_SCOPE");
				}
			} catch (Exception e) {
				if(e.getMessage().equals("INCORRECT_SCOPE")) {
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					response.getOutputStream().write("INCORRECT_SCOPE".getBytes(StandardCharsets.UTF_8));
				}
				else {
					response.setStatus(HttpStatus.FORBIDDEN.value());
					response.getOutputStream().write("INVALID_USERNAME_INVALID_TOKEN".getBytes(StandardCharsets.UTF_8));
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
