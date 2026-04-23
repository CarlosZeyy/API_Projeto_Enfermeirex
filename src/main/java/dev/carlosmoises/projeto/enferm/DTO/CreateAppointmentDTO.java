package dev.carlosmoises.projeto.enferm.DTO;

import dev.carlosmoises.projeto.enferm.model.StatusAppointment;

import java.time.LocalDateTime;

public record CreateAppointmentDTO(Long patientId, LocalDateTime data, StatusAppointment status) {
}
