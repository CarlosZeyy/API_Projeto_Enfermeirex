package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.DocumentResponseDTO;
import dev.carlosmoises.projeto.enferm.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/uploads")
public class DocumentController {
    private final DocumentService documentService;

    DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("patientId") Long patientId) {
        var upload = documentService.createFileUpload(patientId, file);

        var response = new ResponseMessage("Upload realizado com sucesso");

        return ResponseEntity.created(URI.create("/uploads/" + upload)).body(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DocumentResponseDTO>> getPatientDocuments(@PathVariable("patientId") Long patientId) {
        var documents = documentService.getPatientDocuments(patientId);
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/patient/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("documentId") Long documentId) throws IOException {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
