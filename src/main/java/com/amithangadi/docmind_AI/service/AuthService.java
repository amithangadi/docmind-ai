package com.amithangadi.docmind_AI.service;

import com.amithangadi.docmind_AI.dto.request.LoginRequest;
import com.amithangadi.docmind_AI.dto.response.LoginResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);
}
