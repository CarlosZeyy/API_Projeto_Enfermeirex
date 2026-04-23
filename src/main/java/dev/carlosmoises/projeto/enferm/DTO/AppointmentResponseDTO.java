package dev.carlosmoises.projeto.enferm.DTO;

import dev.carlosmoises.projeto.enferm.model.StatusAppointment;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(Long appointmentId,Long patientId, LocalDateTime data, StatusAppointment status) {
}
