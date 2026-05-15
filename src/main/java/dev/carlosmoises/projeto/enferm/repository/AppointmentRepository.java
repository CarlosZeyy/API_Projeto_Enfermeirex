package dev.carlosmoises.projeto.enferm.repository;

import dev.carlosmoises.projeto.enferm.model.Appointment;
import dev.carlosmoises.projeto.enferm.model.StatusAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByData(LocalDateTime data);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDataBeforeAndStatus(LocalDateTime dateTime, StatusAppointment status);

    List<Appointment> findByDataBetweenOrderByDataAsc(LocalDateTime start, LocalDateTime end);
}
