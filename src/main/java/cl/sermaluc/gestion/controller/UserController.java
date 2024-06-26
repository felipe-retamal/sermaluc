package cl.sermaluc.gestion.controller;

import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.bezkoder.spring.hibernate.onetomany.exception.ResourceNotFoundException;
//import com.bezkoder.spring.hibernate.onetomany.model.Comment;

import cl.sermaluc.gestion.model.Phone;
import cl.sermaluc.gestion.model.User;
import cl.sermaluc.gestion.payload.response.MessageResponse;
import cl.sermaluc.gestion.repository.PhoneRepository;
import cl.sermaluc.gestion.repository.UserRepository;
import cl.sermaluc.gestion.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PhoneRepository phoneRepository;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Value("${correo.regex}")
	private String regex_correo;

	@Value("${pwd.regex}")
	private String regex_pwd;

	private String secret = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";

	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(errors.getAllErrors().get(0).getDefaultMessage()));
		}
		try {
			if (!isValidEmailAddress(user.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Correo mal estructurado"));
			}
			if (!isValidPwd(user.getPassword())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Password mal estructurado"));
			}
			if (userRepository.existsByName(user.getName())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("El usuario ya esta registrado"));
			}
			if (userRepository.existsByEmail(user.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("El correo ya esta registrado"));
			}
			String token = getJWTToken(user.getName());
			user.setToken(token);
			Phone phone = user.getPhones().get(0);
			user.setPhones(null);
			User userSave = userRepository.save(user);
			phone.setUser(userSave);
//		    Phone phone = userRepository.findById(userSave.getId()).map(user -> {
//			userSave.getPhones().add(user.getPhones().);
		       phoneRepository.save(phone);
//		      }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

		    
//			Phone phoneTemp = user.getPhones().get(0);
//			phoneTemp.setUser(userSave);
//			Phone phoneSave = phoneRepository.save(phoneTemp);
//			user.getPhones().get(0).setUser(userSave)
			
//			user.getPhones().add(user.getPhones());
//			Phone phoneSave = PhoneRepository.save(commentRequest);

		     return ResponseEntity.ok(user) ;
		} catch (Exception e) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("error"));
		}
	}

	private String getJWTToken(String name) {
		String token = Jwts
				.builder()
				.setId("sermaluc")
				.setSubject(name)
				.claim("authorities", null) //para este ejercicio no se consideran privilegios
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
		return "Bearer " + token;
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = regex_correo;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}
	public boolean isValidPwd(String pwd) {
		String ePattern = regex_pwd;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(pwd);
		return m.matches();
	}
}
