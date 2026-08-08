package com.amithangadi.docmind_AI.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amithangadi.docmind_AI.dto.request.RenameDocumentRequest;
import com.amithangadi.docmind_AI.dto.response.DocumentDetailsResponse;
import com.amithangadi.docmind_AI.dto.response.DocumentResponse;
import com.amithangadi.docmind_AI.entity.Document;
import com.amithangadi.docmind_AI.service.DocumentService;
import com.amithangadi.docmind_AI.service.DocumentTextExtractor;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	private final DocumentService documentService;
	
	public DocumentController(DocumentService documentService)
	{
		this.documentService = documentService;
	}
	
	@PostMapping(value = "/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DocumentResponse> uploadDocument(
			@RequestParam("file") MultipartFile file,
			
			@RequestParam(value = "description", required = false)
			String description,
			
			 Authentication authentication
			)
	{
		String email = authentication.getName();
		
		DocumentResponse response = documentService.uploadDocument(file, 
				description, 
				email);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping
	public ResponseEntity<Page<DocumentResponse>> getMyDocuments(

	        Authentication authentication,

	        @RequestParam(defaultValue = "0")
	        int page,

	        @RequestParam(defaultValue = "10")
	        int size) {

	    return ResponseEntity.ok(

	            documentService.getMyDocuments(
	                    authentication.getName(),
	                    page,
	                    size)

	    );

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DocumentDetailsResponse> getDocument(
	        @PathVariable Long id,
	        Authentication authentication) {

	    return ResponseEntity.ok(
	            documentService.getDocument(
	                    id,
	                    authentication.getName()
	            )
	    );
	}
	
	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> downloadDocument(
	        @PathVariable Long id,
	        Authentication authentication) {

	    Resource resource =
	            documentService.downloadDocument(
	                    id,
	                    authentication.getName());

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" +
	                            resource.getFilename() +
	                            "\"")
	            .body(resource);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<DocumentDetailsResponse> renameDocument(

	        @PathVariable Long id,

	        @Valid
	        @RequestBody RenameDocumentRequest request,

	        Authentication authentication) {

	    return ResponseEntity.ok(

	            documentService.renameDocument(
	                    id,
	                    request,
	                    authentication.getName())

	    );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDocument(

	        @PathVariable Long id,

	        Authentication authentication) {

	    documentService.deleteDocument(
	            id,
	            authentication.getName());

	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}/text")
	public ResponseEntity<String> extractText(
	        @PathVariable Long id,
	        Authentication authentication) {

	    String text = documentService.extractDocumentText(
	            id,
	            authentication.getName());

	    return ResponseEntity.ok(text);
	}
	
	@PostMapping("/{id}/process-text")
	public ResponseEntity<Void> processDocumentText(
	        @PathVariable Long id,
	        Authentication authentication) {

	    documentService.processDocumentText(
	            id,
	            authentication.getName());

	    return ResponseEntity.ok().build();
	}
}
