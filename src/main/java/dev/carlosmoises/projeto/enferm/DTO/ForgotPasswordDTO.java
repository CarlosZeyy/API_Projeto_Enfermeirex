package dev.carlosmoises.projeto.enferm.DTO;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(@NotBlank String identification) {
}
