package dev.carlosmoises.projeto.enferm.DTO;

import java.time.LocalDateTime;

public record DocumentResponseDTO(Long documentId, String fileName, String filePath, Long patientId,
                                  LocalDateTime createdAt) {
}
