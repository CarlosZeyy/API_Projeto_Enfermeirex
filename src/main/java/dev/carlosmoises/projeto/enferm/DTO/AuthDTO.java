package dev.carlosmoises.projeto.enferm.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthDTO(@NotBlank String login, @NotBlank String password) {
}
