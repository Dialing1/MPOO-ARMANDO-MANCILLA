package mx.unam.fi.smartvend.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReabastecimientoRequest(@NotNull @Positive Integer cantidad) { }
