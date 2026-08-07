package com.amithangadi.docmind_AI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.amithangadi.docmind_AI.entity.Document;
import com.amithangadi.docmind_AI.entity.User;

public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document>{

	Page<Document> findByUser(User user, Pageable pageable);
	
	Optional<Document> findByIdAndUser(Long id, User user);
	
}
