package dev.carlosmoises.projeto.enferm.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(@NotBlank @Size(min = 6) String currentPassword,
                                @NotBlank @Size(min = 6) String newPassword) {
}
