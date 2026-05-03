package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.AnnotationResponseDTO;
import dev.carlosmoises.projeto.enferm.DTO.CreateAnnotationDTO;
import dev.carlosmoises.projeto.enferm.model.Annotation;
import dev.carlosmoises.projeto.enferm.repository.AnnotationRepository;
import dev.carlosmoises.projeto.enferm.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationService {
    private final AnnotationRepository annotationRepository;
    private final PatientRepository patientRepository;

    AnnotationService(AnnotationRepository annotationRepository, PatientRepository patientRepository) {
        this.annotationRepository = annotationRepository;
        this.patientRepository = patientRepository;
    }

    public Long createAnnotation(CreateAnnotationDTO createAnnotationDTO) {
        var patient = patientRepository.findById(createAnnotationDTO.patientId()).orElseThrow(() -> new RuntimeException("Id do paciente não encontrado."));

        var annotation = new Annotation();

        annotation.setPatient(patient);
        annotation.setText(createAnnotationDTO.text());

        var annotationSaved = annotationRepository.save(annotation);

        return annotationSaved.getAnnotationId();
    }

    public List<AnnotationResponseDTO> getPatientAnnotations(Long patientId) {
        return annotationRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream().map(
                (annotation -> new AnnotationResponseDTO(
                        annotation.getAnnotationId(),
                        annotation.getPatient().getId(),
                        annotation.getText(),
                        annotation.getCreatedAt()
                ))
        ).toList();
    }
}
