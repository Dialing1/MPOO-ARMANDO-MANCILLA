package mx.unam.fi.smartvend.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import mx.unam.fi.smartvend.domain.pago.MetodoPago;

/** codigoProducto es opcional: si viene, debe coincidir con el de la URL. */
public record CompraRequest(
        String codigoProducto,
        @NotNull MetodoPago metodoPago,
        @NotNull @Positive Double cantidadRecibida) { }
