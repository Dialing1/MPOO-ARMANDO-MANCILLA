package mx.unam.fi.smartvend.infrastructure.pago;

import mx.unam.fi.smartvend.domain.pago.EstadoCobro;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;

/** Pagos en efectivo: conserva el comportamiento de la Practica 9 (genera cambio). */
public class MonederoEfectivo implements Monedero {

    private double fondos;

    public MonederoEfectivo(double fondosIniciales) {
        if (Double.isNaN(fondosIniciales) || Double.isInfinite(fondosIniciales) || fondosIniciales < 0)
            throw new IllegalArgumentException("Los fondos no pueden ser negativos");
        this.fondos = fondosIniciales;
    }

    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (Double.isNaN(precio) || Double.isNaN(cantidadRecibida)
                || Double.isInfinite(precio) || Double.isInfinite(cantidadRecibida)
                || precio <= 0 || cantidadRecibida + 1e-9 < precio)
            return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        double cambio = Math.max(0.0, Math.round((cantidadRecibida - precio) * 100) / 100.0);
        if (cambio > fondos)
            return new ResultadoCobro(EstadoCobro.SIN_FONDOS_SUFICIENTES, 0.0);
        fondos += precio;   // solo el precio es ingreso; el cambio se regresa al usuario
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, cambio);
    }

    @Override
    public double consultarFondos() { return fondos; }
}
