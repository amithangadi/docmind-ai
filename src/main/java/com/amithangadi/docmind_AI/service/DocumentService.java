package com.amithangadi.docmind_AI.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.amithangadi.docmind_AI.dto.request.RenameDocumentRequest;
import com.amithangadi.docmind_AI.dto.response.DocumentDetailsResponse;
import com.amithangadi.docmind_AI.dto.response.DocumentResponse;
import com.amithangadi.docmind_AI.dto.response.DocumentSummaryResponse;

public interface DocumentService {

    DocumentResponse uploadDocument(
            MultipartFile file,
            String description,
            String email);

    Page<DocumentResponse> getMyDocuments(
            String email,
            int page,
            int size);
    
    DocumentDetailsResponse getDocument(Long id, String email);
    
    Resource downloadDocument(Long id, String email);
    
    DocumentDetailsResponse renameDocument(
            Long id,
            RenameDocumentRequest request,
            String email);
    
    void deleteDocument(Long id, String email);
    
    Page<DocumentSummaryResponse> getDocuments(
            String email,
            String keyword,
            String fileType,
            int page,
            int size,
            String sortBy,
            String direction);
}