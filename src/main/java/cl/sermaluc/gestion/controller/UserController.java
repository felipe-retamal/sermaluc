package cl.sermaluc.gestion.controller;

import java.util.ArrayList;
import java.util.Date;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cl.sermaluc.gestion.model.Phone;
import cl.sermaluc.gestion.model.User;
import cl.sermaluc.gestion.payload.request.RegisterRequest;
import cl.sermaluc.gestion.payload.response.MessageResponse;
import cl.sermaluc.gestion.repository.UserRepository;
import cl.sermaluc.gestion.security.jwt.AuthTokenFilter;
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
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Value("${correo.regex}")
	private String regex;


	private String secret = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, Errors errors) {
		if (errors.hasErrors()) {
			//        return new ResponseEntity(new ApiErrors(errors), HttpStatus.BAD_REQUEST);
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(errors.getAllErrors().get(0).getDefaultMessage()));
		}
		try {
			if (userRepository.existsByName(user.getName())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("El usuario ya esta registrado"));
			}
			if (!isValidEmailAddress(user.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Correo mal estructurado"));
			}
			if (userRepository.existsByEmail(user.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("El correo ya esta registrado"));
			}
			String token = getJWTToken(user.getName());
			user.setToken(token);
			return ResponseEntity.ok(userRepository.save(user)) ;
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
        String ePattern = regex;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
}
