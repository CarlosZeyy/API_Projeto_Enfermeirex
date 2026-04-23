package dev.carlosmoises.projeto.enferm.repository;

import dev.carlosmoises.projeto.enferm.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByData(LocalDateTime data);

    List<Appointment> findByPatientPatientId(Long patientId);
}
