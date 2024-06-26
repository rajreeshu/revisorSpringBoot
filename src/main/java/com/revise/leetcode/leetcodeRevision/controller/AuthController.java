package com.revise.leetcode.leetcodeRevision.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revise.leetcode.leetcodeRevision.config.JwtHelper;
import com.revise.leetcode.leetcodeRevision.dto.JwtRequest;
import com.revise.leetcode.leetcodeRevision.dto.JwtResponse;
import com.revise.leetcode.leetcodeRevision.dto.UserDto;
import com.revise.leetcode.leetcodeRevision.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtHelper jwtHelper;

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody JwtRequest request) {
		try {
			this.doAuthenticate(request.getEmail(), request.getPassword());

			logger.info("after authentication");

			UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
			logger.info(userDetails.toString());
			String token = this.helper.generateToken(userDetails);

			JwtResponse response = JwtResponse.builder().token(token).username(userDetails.getUsername()).build();
//		        return new ResponseEntity<>(response, HttpStatus.OK);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<JwtResponse> signup(@Valid @RequestBody UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		UserDto newUser = userService.addUser(userDto);

		UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
		String token = this.helper.generateToken(userDetails);

		JwtResponse response = JwtResponse.builder().token(token).username(userDetails.getUsername()).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			logger.info("inside try");
			manager.authenticate(authentication);
			logger.info("after auth");

		} catch (Exception e) {
			throw new BadCredentialsException(" Invalid Username or Password  !!");
		}

	}

	@GetMapping("/validateToken")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authToken) {
		try {
			// Assuming the token is sent in the header with prefix 'Bearer '
			if (authToken != null && authToken.startsWith("Bearer ")) {
				authToken = authToken.substring(7);
			}

			boolean isExpired = jwtHelper.isTokenExpired(authToken);

			if (!isExpired) {
				return ResponseEntity.ok(true);
			} else {
				return ResponseEntity.badRequest().body(false);
			}
		} catch (Exception e) {
			logger.error("Exception in validating token: ", e);
			return ResponseEntity.badRequest().body(false);
		}
	}

}
