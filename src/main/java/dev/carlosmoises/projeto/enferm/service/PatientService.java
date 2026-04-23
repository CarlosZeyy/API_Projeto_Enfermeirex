package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.CreatePatientDTO;
import dev.carlosmoises.projeto.enferm.DTO.PatientResponseDTO;
import dev.carlosmoises.projeto.enferm.DTO.UpdatePatientDTO;
import dev.carlosmoises.projeto.enferm.model.Patient;
import dev.carlosmoises.projeto.enferm.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Long createPatient(CreatePatientDTO createPatientDTO) {
        var patient = new Patient(null, createPatientDTO.name(), createPatientDTO.phone(), createPatientDTO.address(), null);

        var patientSaved = patientRepository.save(patient);

        return patientSaved.getPatientId();
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patient -> new PatientResponseDTO(
                patient.getPatientId(),
                patient.getName(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getObs()
        )).toList();
    }

    public PatientResponseDTO getPatientById(Long id) {
        var patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        return new PatientResponseDTO(
                patient.getPatientId(),
                patient.getName(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getObs());
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public PatientResponseDTO updatePatientById(Long id, UpdatePatientDTO updatePatientDTO) {
        var patient = patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (updatePatientDTO.name() != null) {
            patient.setName(updatePatientDTO.name());
        }

        if (updatePatientDTO.phone() != null) {
            patient.setPhone(updatePatientDTO.phone());
        }

        if (updatePatientDTO.address() != null) {
            patient.setAddress(updatePatientDTO.address());
        }

        patient.setObs(updatePatientDTO.obs());

        patientRepository.save(patient);

        return new PatientResponseDTO(
                patient.getPatientId(),
                patient.getName(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getObs()
        );
    }
}
