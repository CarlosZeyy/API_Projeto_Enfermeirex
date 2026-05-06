package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.DocumentResponseDTO;
import dev.carlosmoises.projeto.enferm.model.Document;
import dev.carlosmoises.projeto.enferm.repository.DocumentRepository;
import dev.carlosmoises.projeto.enferm.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private final PatientRepository patientRepository;
    private final DocumentRepository documentRepository;

    public DocumentService(PatientRepository patientRepository, DocumentRepository documentRepository) {
        this.patientRepository = patientRepository;
        this.documentRepository = documentRepository;
    }

    public Long createFileUpload(Long patientId, MultipartFile file) {
        var patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("ID do paciente não encontrado"));

        if (file.isEmpty()) {
            throw new RuntimeException("Nenhum arquivo selecionado para fazer upload.");
        }

        var newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path uploadDir = Path.of("uploads");

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(newFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            var document = new Document();

            document.setPatient(patient);
            document.setFileName(file.getOriginalFilename());
            document.setFilePath(filePath.toString());

            var documentSaved = documentRepository.save(document);

            return documentSaved.getDocumentId();

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo físico: " + e.getMessage());
        }
    }

    public List<DocumentResponseDTO> getPatientDocuments(Long patientId) {
        return documentRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream().map(
                (document -> new DocumentResponseDTO(
                        document.getDocumentId(),
                        document.getFileName(),
                        document.getFilePath(),
                        document.getPatient().getId(),
                        document.getCreatedAt()
                ))
        ).toList();
    }

    public void deleteDocument(Long documentId) throws IOException {
        try {
            var document = documentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Arquivo não encontrado."));
            Files.deleteIfExists(Path.of(document.getFilePath()));
        } catch (IOException error) {
            throw new RuntimeException(error);
        }

        documentRepository.deleteById(documentId);
    }
}
