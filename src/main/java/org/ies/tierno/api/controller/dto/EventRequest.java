package org.ies.tierno.api.controller.dto;

import jakarta.validation.constraints.*;
import org.ies.tierno.api.model.Category;

import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank
        @Size(max = 100, message = "El titulo tiene que tener un maximo de 100 caracteres")
        String title,
        @NotBlank
        Category category,
        @NotNull
        @Future(message = "Debe ser futura")
        LocalDateTime startsAt,
        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad minima es 1")
        @Max(value = 500, message = "La capacidad maxima es 500")
        Integer capacity
) {
}