package com.amithangadi.docmind_AI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amithangadi.docmind_AI.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
