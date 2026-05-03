package dev.carlosmoises.projeto.enferm.repository;

import dev.carlosmoises.projeto.enferm.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
