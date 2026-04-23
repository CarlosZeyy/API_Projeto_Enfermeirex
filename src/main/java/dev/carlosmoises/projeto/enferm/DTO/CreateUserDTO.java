package dev.carlosmoises.projeto.enferm.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserDTO(@NotBlank String email, @NotBlank String password,

                            @NotBlank
                            @Pattern(regexp = "^COREN-[A-Z]{2} \\d{4,6}-(ENF|TE|AE)$", message = "Formato de COREN inválido. Ex: COREN-SP 123456-ENF")
                            String coren) {
}
