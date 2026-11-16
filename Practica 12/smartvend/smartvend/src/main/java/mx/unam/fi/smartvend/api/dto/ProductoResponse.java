package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.producto.CategoriaProducto;

import java.time.LocalDate;

public record ProductoResponse(
        String codigo,
        String nombre,
        LocalDate fechaCaducidad,
        CategoriaProducto categoria,
        double precio,
        int existencias) { }
