package com.amithangadi.docmind_AI.service;

import com.amithangadi.docmind_AI.dto.request.RegisterRequest;
import com.amithangadi.docmind_AI.dto.response.UserResponse;

public interface UserService {

	UserResponse register(RegisterRequest request);
}
