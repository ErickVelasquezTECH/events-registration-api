package org.ies.tierno.api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(

        @NotBlank
        @Size(max = 100, message = "El nombre tiene que tener un maximo de 100 caracteres")
        String attendeeName,
        @NotBlank
        @Email
        String attendeeEmail
) {
}