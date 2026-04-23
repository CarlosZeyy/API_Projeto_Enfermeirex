package dev.carlosmoises.projeto.enferm.DTO;

import dev.carlosmoises.projeto.enferm.model.StatusAppointment;

import java.time.LocalDateTime;

public record UpdateAppointmentDTO(LocalDateTime data, StatusAppointment status) {
}
