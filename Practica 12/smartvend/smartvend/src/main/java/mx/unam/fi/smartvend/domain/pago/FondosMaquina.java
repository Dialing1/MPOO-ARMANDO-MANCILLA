package mx.unam.fi.smartvend.domain.pago;

/** Fondos acumulados de forma independiente en cada monedero y el total global. */
public record FondosMaquina(double efectivo, double tarjeta, double total) { }
