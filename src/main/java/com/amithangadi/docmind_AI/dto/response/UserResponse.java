package com.amithangadi.docmind_AI.dto.response;

import com.amithangadi.docmind_AI.entity.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Role role;
}
