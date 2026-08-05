package com.amithangadi.docmind_AI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amithangadi.docmind_AI.dto.request.LoginRequest;
import com.amithangadi.docmind_AI.dto.request.RegisterRequest;
import com.amithangadi.docmind_AI.dto.response.LoginResponse;
import com.amithangadi.docmind_AI.dto.response.UserResponse;
import com.amithangadi.docmind_AI.service.AuthService;
import com.amithangadi.docmind_AI.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	
	private final AuthService authService;
	
	public AuthController(UserService userService, AuthService authService)
	{
		this.userService = userService;
		this.authService = authService;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(
			@Valid @RequestBody RegisterRequest request) {
		
			UserResponse response = userService.register(request);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest request)
	{
		return ResponseEntity.ok(authService.login(request));
	}
}
