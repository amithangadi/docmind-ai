package com.amithangadi.docmind_AI.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amithangadi.docmind_AI.dto.request.RenameDocumentRequest;
import com.amithangadi.docmind_AI.dto.response.DocumentDetailsResponse;
import com.amithangadi.docmind_AI.dto.response.DocumentResponse;
import com.amithangadi.docmind_AI.dto.response.DocumentSummaryResponse;
import com.amithangadi.docmind_AI.entity.Document;
import com.amithangadi.docmind_AI.entity.User;
import com.amithangadi.docmind_AI.exception.ResourceNotFoundException;
import com.amithangadi.docmind_AI.repository.DocumentRepository;
import com.amithangadi.docmind_AI.repository.UserRepository;

import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
    );

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DocumentResponse uploadDocument(
            MultipartFile file,
            String description,
            String email) {

        // Validate uploaded file
        validateFile(file);

        // Get logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Generate unique filename
        String storedFileName = generateStoredFileName(file);

        // Save file
        Path savedPath = saveFile(file, storedFileName);

        // Save metadata
        Document document = new Document();
        document.setOriginalFileName(file.getOriginalFilename());
        document.setStoredFileName(storedFileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStoragePath(savedPath.toString());
        document.setDescription(description);
        document.setUser(user);

        Document savedDocument = documentRepository.save(document);

        return buildResponse(savedDocument);
    }

    @Override
    public Page<DocumentResponse> getMyDocuments(
            String email,
            int page,
            int size) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Document> documents =
                documentRepository.findByUser(user, pageable);

        return documents.map(this::buildResponse);
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type.");
        }

        long maxSize = 20 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Maximum file size is 20 MB.");
        }
    }

    private String generateStoredFileName(MultipartFile file) {

        String originalName = file.getOriginalFilename();

        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        return UUID.randomUUID() + extension;
    }

    private Path saveFile(MultipartFile file, String storedFileName) {

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path destination = uploadPath.resolve(storedFileName);

            file.transferTo(destination);

            return destination;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to save file.", ex);
        }
    }

    private DocumentResponse buildResponse(Document document) {

        DocumentResponse response = new DocumentResponse();

        response.setId(document.getId());
        response.setOriginalFileName(document.getOriginalFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setDescription(document.getDescription());

        return response;
    }
    
    private DocumentDetailsResponse buildDocumentDetailsResponse(Document document) {

        DocumentDetailsResponse response = new DocumentDetailsResponse();

        response.setId(document.getId());
        response.setOriginalFileName(document.getOriginalFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setDescription(document.getDescription());
        response.setStoragePath(document.getStoragePath());

        return response;
    }
    
    @Override
    public DocumentDetailsResponse getDocument(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Document document = documentRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document not found"));

        DocumentDetailsResponse response = new DocumentDetailsResponse();

        response.setId(document.getId());
        response.setOriginalFileName(document.getOriginalFileName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        response.setDescription(document.getDescription());
        response.setStoragePath(document.getStoragePath());

        return response;
    }
    
    @Override
    public Resource downloadDocument(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Document document = documentRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document not found"));

        try {

            Path filePath = Paths.get(document.getStoragePath());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found.");
            }

            return resource;

        } catch (MalformedURLException ex) {

            throw new RuntimeException("Unable to download file.", ex);

        }
    }
    
    @Override
    public DocumentDetailsResponse renameDocument(
            Long id,
            RenameDocumentRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Document document = documentRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document not found"));

        document.setOriginalFileName(request.getOriginalFileName());
        document.setDescription(request.getDescription());

        Document updatedDocument = documentRepository.save(document);

        return buildDocumentDetailsResponse(updatedDocument);
    }
    
    @Override
    public void deleteDocument(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Document document = documentRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document not found"));

        Path filePath = Paths.get(document.getStoragePath());

        try {

            Files.deleteIfExists(filePath);

        } catch (IOException ex) {

            throw new RuntimeException("Unable to delete file.", ex);

        }

        documentRepository.delete(document);
    }

	@Override
	public Page<DocumentSummaryResponse> getDocuments(String email, String keyword, String fileType, int page, int size,
			String sortBy, String direction) {
		// TODO Auto-generated method stub
		return null;
	}
}