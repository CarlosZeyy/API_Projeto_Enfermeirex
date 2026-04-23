package dev.carlosmoises.projeto.enferm.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePatientDTO(@NotBlank String name, @NotBlank @Size(min = 10, max = 15) String phone, String address) {
}
