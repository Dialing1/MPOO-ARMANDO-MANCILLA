package mx.unam.fi.parkmanager.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import mx.unam.fi.parkmanager.exception.DescuentoInvalidoException;

/**
 * Clase base (abstracta) de las modalidades de boleto.
 * Sabe guardar su descuento y calcular su precio final; lo unico que cada
 * hija decide es su identificador y su precio base (polimorfismo).
 */
public abstract class Boleto {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private BigDecimal descuento = BigDecimal.ZERO;   // porcentaje 0..100

    /** Identificador de la modalidad. */
    public abstract TipoBoleto tipo();

    /** Precio base: cada modalidad lo resuelve por si misma. */
    public abstract BigDecimal precioBase();

    public BigDecimal getDescuento() {
        return descuento;
    }

    /** Reemplaza el descuento de esta modalidad (0 a 100 inclusive). */
    public void configurarDescuento(BigDecimal porcentaje) {
        if (porcentaje == null || porcentaje.signum() < 0 || porcentaje.compareTo(CIEN) > 0) {
            throw new DescuentoInvalidoException("El porcentaje debe estar entre 0 y 100");
        }
        this.descuento = porcentaje;
    }

    /** Desactiva el descuento; es idempotente. */
    public void quitarDescuento() {
        this.descuento = BigDecimal.ZERO;
    }

    /** Precio unitario con el descuento vigente, redondeado a centavos. */
    public BigDecimal precioFinal() {
        BigDecimal factor = CIEN.subtract(descuento).divide(CIEN);
        return precioBase().multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
