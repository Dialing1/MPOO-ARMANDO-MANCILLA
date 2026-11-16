package mx.unam.fi.smartvend.api.dto;

import mx.unam.fi.smartvend.domain.compra.EstadoCompra;
import mx.unam.fi.smartvend.domain.pago.MetodoPago;

public record CompraResponse(
        EstadoCompra estado,
        String codigoProducto,
        String nombreProducto,
        MetodoPago metodoPago,
        double cambio) { }
