package mx.unam.fi.parkmanager.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import mx.unam.fi.parkmanager.dto.CompraRequest;
import mx.unam.fi.parkmanager.dto.CompraResponse;
import mx.unam.fi.parkmanager.exception.CantidadInvalidaException;
import mx.unam.fi.parkmanager.exception.SolicitudInvalidaException;
import mx.unam.fi.parkmanager.model.Boleto;
import mx.unam.fi.parkmanager.model.Taquilla;
import mx.unam.fi.parkmanager.model.TipoBoleto;

@Service
public class CompraService {

    private static final BigDecimal MINIMO = BigDecimal.ONE;
    private static final BigDecimal MAXIMO = BigDecimal.TEN;

    private final Taquilla taquilla;

    public CompraService(Taquilla taquilla) {
        this.taquilla = taquilla;
    }

    /** Compra atomica: primero se valida y se calcula todo; el estado cambia al final. */
    public synchronized CompraResponse comprar(CompraRequest solicitud) {
        if (solicitud == null || solicitud.tipo() == null || solicitud.cantidad() == null) {
            throw new SolicitudInvalidaException("Los campos 'tipo' y 'cantidad' son obligatorios");
        }
        TipoBoleto tipo = TipoBoleto.desde(solicitud.tipo());
        int cantidad = validarCantidad(solicitud.cantidad());

        Boleto modalidad = taquilla.modalidad(tipo);
        BigDecimal precioBase = modalidad.precioBase();          // polimorfismo
        BigDecimal unitario = modalidad.precioFinal();
        BigDecimal total = unitario.multiply(BigDecimal.valueOf(cantidad));

        taquilla.registrarVenta(cantidad, total);                // unico punto que modifica estado

        BigDecimal descuento = modalidad.getDescuento();
        if (descuento.scale() < 0) {
            descuento = descuento.setScale(0);
        }
        return new CompraResponse(tipo.name(), cantidad, precioBase, descuento,
                unitario, total, taquilla.getIngresos());
    }

    private static int validarCantidad(BigDecimal cantidad) {
        boolean entera = cantidad.signum() == 0 || cantidad.stripTrailingZeros().scale() <= 0;
        if (!entera || cantidad.compareTo(MINIMO) < 0 || cantidad.compareTo(MAXIMO) > 0) {
            throw new CantidadInvalidaException("La cantidad debe ser un entero entre 1 y 10");
        }
        return cantidad.intValueExact();
    }
}
