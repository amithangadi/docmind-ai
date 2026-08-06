package com.amithangadi.docmind_AI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amithangadi.docmind_AI.entity.Document;
import com.amithangadi.docmind_AI.entity.User;

public interface DocumentRepository extends JpaRepository<Document, Long>{

	List<Document> findByUser(User user);
	
}
