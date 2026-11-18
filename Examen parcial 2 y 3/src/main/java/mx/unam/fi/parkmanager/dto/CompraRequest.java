package mx.unam.fi.parkmanager.dto;

import java.math.BigDecimal;

/** La cantidad llega como numero generico para poder rechazar 2.5 en vez de truncarlo. */
public record CompraRequest(String tipo, BigDecimal cantidad) { }
