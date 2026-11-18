package mx.unam.fi.parkmanager.dto;

import java.math.BigDecimal;

public record DescuentoResponse(String tipo, BigDecimal porcentaje, String mensaje) { }
