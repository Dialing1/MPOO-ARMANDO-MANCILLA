package mx.unam.fi.parkmanager.dto;

import java.math.BigDecimal;

public record CompraResponse(String tipo, int cantidad, BigDecimal precioBase,
                             BigDecimal descuentoAplicado, BigDecimal precioUnitarioFinal,
                             BigDecimal total, BigDecimal ingresosAcumulados) { }
