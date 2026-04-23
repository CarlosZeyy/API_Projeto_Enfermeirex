package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.AppointmentResponseDTO;
import dev.carlosmoises.projeto.enferm.DTO.CreateAppointmentDTO;
import dev.carlosmoises.projeto.enferm.DTO.UpdateAppointmentDTO;
import dev.carlosmoises.projeto.enferm.model.Appointment;
import dev.carlosmoises.projeto.enferm.model.StatusAppointment;
import dev.carlosmoises.projeto.enferm.repository.AppointmentRepository;
import dev.carlosmoises.projeto.enferm.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public Long createAppointment(CreateAppointmentDTO createAppointmentDTO) {
        if (appointmentRepository.existsByData(createAppointmentDTO.data())) {
            throw new RuntimeException("Ja existe um agendamento marcado nesse horario");
        }

        var patient = patientRepository.findById(createAppointmentDTO.patientId()).orElseThrow(() -> new RuntimeException("Id de paciente não encontrado."));

        var appointment = new Appointment();

        appointment.setData(createAppointmentDTO.data());
        appointment.setPatient(patient);
        appointment.setStatus(StatusAppointment.AGENDADO);

        var appointmentSaved = appointmentRepository.save(appointment);
        return appointmentSaved.getAppointmentId();
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointment -> new AppointmentResponseDTO(
                appointment.getAppointmentId(),
                appointment.getPatient().getPatientId(),
                appointment.getData(),
                appointment.getStatus()
        )).toList();
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        var appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        return new AppointmentResponseDTO(
                appointment.getAppointmentId(),
                appointment.getPatient().getPatientId(),
                appointment.getData(),
                appointment.getStatus()
        );
    }

    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientPatientId(patientId).stream().map(
                appointment -> new AppointmentResponseDTO(
                        appointment.getAppointmentId(),
                        appointment.getPatient().getPatientId(),
                        appointment.getData(),
                        appointment.getStatus()
                )).toList();
    }

    public AppointmentResponseDTO updateAppointment(Long id, UpdateAppointmentDTO updateAppointmentDTO) {
        var appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (updateAppointmentDTO.data() != null) {
            if (appointmentRepository.existsByData(updateAppointmentDTO.data())) {
                throw new RuntimeException("Já existe um novo agendamento para está data, escolha outro horario");
            }
            appointment.setData(updateAppointmentDTO.data());
        }

        if (updateAppointmentDTO.status() != null) {
            appointment.setStatus(updateAppointmentDTO.status());
        }

        appointmentRepository.save(appointment);

        return new AppointmentResponseDTO(
                appointment.getAppointmentId(),
                appointment.getPatient().getPatientId(),
                appointment.getData(),
                appointment.getStatus()
        );
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
