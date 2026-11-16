package mx.unam.fi.smartvend.infrastructure.pago;

import mx.unam.fi.smartvend.domain.pago.EstadoCobro;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;

/**
 * Pagos procesados electronicamente. No genera cambio: se cobra exactamente el
 * precio del producto y los fondos se guardan aparte de los de efectivo.
 */
public class MonederoTarjeta implements Monedero {

    private double fondos;

    public MonederoTarjeta(double fondosIniciales) {
        if (Double.isNaN(fondosIniciales) || Double.isInfinite(fondosIniciales) || fondosIniciales < 0)
            throw new IllegalArgumentException("Los fondos no pueden ser negativos");
        this.fondos = fondosIniciales;
    }

    @Override
    public ResultadoCobro cobrar(double precio, double cantidadRecibida) {
        if (Double.isNaN(precio) || Double.isNaN(cantidadRecibida)
                || Double.isInfinite(precio) || Double.isInfinite(cantidadRecibida)
                || precio <= 0 || Math.abs(cantidadRecibida - precio) > 1e-9)
            return new ResultadoCobro(EstadoCobro.MONTO_INVALIDO, 0.0);
        fondos += precio;
        return new ResultadoCobro(EstadoCobro.COBRO_REALIZADO, 0.0);
    }

    @Override
    public double consultarFondos() { return fondos; }
}
