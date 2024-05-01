package cl.sermaluc.gestion.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.sermaluc.gestion.model.Phone;
import cl.sermaluc.gestion.model.User;
import cl.sermaluc.gestion.repository.PhoneRepository;
import cl.sermaluc.gestion.repository.UserRepository;
import cl.sermaluc.gestion.security.jwt.JwtUtils;

@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
//	@Autowired private AuthenticationManager authenticationManager;
	@MockBean UserRepository userRepository;
	@MockBean PhoneRepository phoneRepository;

	@MockBean PasswordEncoder encoder;
	@MockBean JwtUtils jwtUtils;
	
	private static final String END_POINT_PATH = "/user";

	@Test
	public void registerUserOk() throws Exception {
        User user = new User();
        user.setName("fretamal");
        user.setEmail("fretamal@correo.com");
        user.setPassword("12345678");
        Phone phone = new Phone();
        phone.setCityCode("2");
        phone.setCountryCode("56");
        phone.setNumber("900000000");
		
        user.getPhones().add(phone);
        
		String requestBody = objectMapper.writeValueAsString(user);
		
		MockHttpServletRequestBuilder build = post(END_POINT_PATH + "/register");
		
//		System.out.println("eeeeeeeeeeeeee" + build.toString());
		
        mockMvc.perform(build.contentType("application/json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
	}
}
