package com.shail.security;

import com.shail.security.controller.MessageController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringSecurityTestConfig.class)	//Passing the security config class details.
@AutoConfigureMockMvc
class SecurityApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired @InjectMocks
	private MessageController messageController;

	@MockBean
	private SecurityContext securityContext;

	@Test
	void contextLoads() {
		
		Assertions.assertNotNull(messageController);
	}

	// Testing if the user with ROLE_ADMIN is able to access the admin api.
	@Test
	@WithUserDetails("shail@mail.com")
	public void adminMessageTestSuccess() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/message").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Testing fail case where user having ROLE_USER should be denied access.
	@Test
	@WithUserDetails("john@mail.com")
	public void adminMessageTestFail() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/message").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
