package com.amithangadi.docmind_AI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RenameDocumentRequest {

	@NotBlank(message = "File name is required")
    @Size(max = 255)
    private String originalFileName;

    @Size(max = 500)
    private String description;

    public RenameDocumentRequest() {
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
