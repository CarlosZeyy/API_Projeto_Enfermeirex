package dev.carlosmoises.projeto.enferm.repository;

import dev.carlosmoises.projeto.enferm.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    List<Annotation> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
