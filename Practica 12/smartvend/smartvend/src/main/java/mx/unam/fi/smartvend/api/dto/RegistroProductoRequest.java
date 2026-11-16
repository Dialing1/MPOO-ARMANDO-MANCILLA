package mx.unam.fi.smartvend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import mx.unam.fi.smartvend.domain.producto.CategoriaProducto;

import java.time.LocalDate;

public record RegistroProductoRequest(
        @NotNull CategoriaProducto tipo,
        @NotBlank String codigo,
        @NotBlank String nombre,
        @NotNull LocalDate fechaCaducidad,
        @NotNull CategoriaProducto categoria,
        @NotNull @Positive Double precio) { }
