package com.amithangadi.docmind_AI.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.amithangadi.docmind_AI.dto.request.LoginRequest;
import com.amithangadi.docmind_AI.dto.response.LoginResponse;
import com.amithangadi.docmind_AI.security.jwt.JwtService;

@Service
public class AuthServiceImpl implements AuthService{

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService)
	{
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
						)
				);
		
		String token = jwtService.generateToken(request.getEmail());
		
		return new LoginResponse(token, "Bearer");
	}
	
	
}
