package com.shail.security;

import com.shail.security.controller.MessageController;
import com.shail.security.filter.JwtTokenAuthenticationFilter;
import com.shail.security.helper.TokenHelper;
import com.shail.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired @InjectMocks
	private MessageController messageController;

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private JwtTokenAuthenticationFilter filter;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	private  String token;

	@BeforeEach
	public void setup() {
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
		Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(new User("test-user", "test-password", authorities));
		token = tokenHelper.generateToken("test-user", authorities);
	}


	@Test
	public void adminMessageTestSuccess() throws Exception {

		/*
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("AUTHORIZATION","Bearer "+token);
		request.setRequestURI("/api/admin/message");
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain();
		filter.doFilter(request, response, filterChain);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

		 */

		mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/message").header("AUTHORIZATION","Bearer "+token).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void adminMessageTestFail() throws Exception {
		String wrongToken = "some_wrong_token";

		mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/message").header("AUTHORIZATION","Bearer "+wrongToken).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isForbidden());

	}
}
