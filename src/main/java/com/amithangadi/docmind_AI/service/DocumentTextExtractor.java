package com.amithangadi.docmind_AI.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;

import com.amithangadi.docmind_AI.entity.Document;

@Service
public class DocumentTextExtractor {

	    public String extractText(Document document) {

	        String fileType = document.getFileType();

	        Path filePath = Path.of(document.getStoragePath());

	        try {

	            if ("application/pdf".equalsIgnoreCase(fileType)) {
	                return extractPdf(filePath);
	            }

	            if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document"
	                    .equalsIgnoreCase(fileType)) {
	                return extractDocx(filePath);
	            }

	            if ("text/plain".equalsIgnoreCase(fileType)) {
	                return extractTextFile(filePath);
	            }

	            throw new IllegalArgumentException(
	                    "Unsupported document type: " + fileType);

	        } catch (IOException ex) {

	            throw new RuntimeException(
	                    "Unable to extract document text.", ex);
	        }
	    }

	    private String extractPdf(Path filePath) throws IOException {

	        try (PDDocument pdfDocument = Loader.loadPDF(filePath.toFile())) {

	            PDFTextStripper stripper = new PDFTextStripper();

	            return stripper.getText(pdfDocument);
	        }
	    }

	    private String extractDocx(Path filePath) throws IOException {

	        try (XWPFDocument document =
	                     new XWPFDocument(Files.newInputStream(filePath))) {

	            XWPFWordExtractor extractor =
	                    new XWPFWordExtractor(document);

	            return extractor.getText();
	        }
	    }

	    private String extractTextFile(Path filePath) throws IOException {

	        return Files.readString(filePath);
	    }
}
