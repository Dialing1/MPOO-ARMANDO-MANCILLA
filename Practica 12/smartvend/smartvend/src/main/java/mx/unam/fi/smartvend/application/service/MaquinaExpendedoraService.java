package mx.unam.fi.smartvend.application.service;

import mx.unam.fi.smartvend.domain.compra.EstadoCompra;
import mx.unam.fi.smartvend.domain.compra.ResultadoCompra;
import mx.unam.fi.smartvend.domain.inventario.Inventario;
import mx.unam.fi.smartvend.domain.inventario.ResultadoEntrega;
import mx.unam.fi.smartvend.domain.inventario.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.inventario.ResultadoRegistro;
import mx.unam.fi.smartvend.domain.pago.FondosMaquina;
import mx.unam.fi.smartvend.domain.pago.MetodoPago;
import mx.unam.fi.smartvend.domain.pago.Monedero;
import mx.unam.fi.smartvend.domain.pago.ResultadoCobro;
import mx.unam.fi.smartvend.domain.producto.Producto;
import mx.unam.fi.smartvend.domain.producto.ProductoInventario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Casos de uso de SmartVend. Depende solo de las interfaces Inventario y Monedero.
 * Es la UNICA puerta para modificar existencias o fondos.
 */
@Service
public class MaquinaExpendedoraService {

    private final Inventario inventario;
    private final Map<MetodoPago, Monedero> monederos = new EnumMap<>(MetodoPago.class);

    public MaquinaExpendedoraService(Inventario inventario,
                                     @Qualifier("monederoEfectivo") Monedero monederoEfectivo,
                                     @Qualifier("monederoTarjeta") Monedero monederoTarjeta) {
        this.inventario = inventario;
        this.monederos.put(MetodoPago.EFECTIVO, monederoEfectivo);
        this.monederos.put(MetodoPago.TARJETA, monederoTarjeta);
    }

    public synchronized ResultadoRegistro registrarProducto(Producto producto, double precio) {
        return inventario.registrarProducto(producto, precio);
    }

    public synchronized Optional<ProductoInventario> buscarProducto(String codigo) {
        return inventario.buscarProducto(codigo);
    }

    public synchronized ResultadoReabastecimiento reabastecer(String codigo, int cantidad) {
        return inventario.reabastecer(codigo, cantidad);
    }

    /** Mismo flujo de compra para cualquier metodo de pago. */
    public synchronized ResultadoCompra comprar(String codigo, MetodoPago metodoPago, double cantidadRecibida) {
        Objects.requireNonNull(metodoPago, "El metodo de pago es obligatorio");

        Optional<ProductoInventario> encontrado = inventario.buscarProducto(codigo);
        if (encontrado.isEmpty())
            return rechazo(EstadoCompra.PRODUCTO_NO_ENCONTRADO, null, metodoPago);
        ProductoInventario pi = encontrado.get();

        if (pi.existencias() <= 0)
            return rechazo(EstadoCompra.SIN_EXISTENCIAS, pi.producto(), metodoPago);
        if (Double.isNaN(cantidadRecibida) || cantidadRecibida + 1e-9 < pi.precio())
            return rechazo(EstadoCompra.PAGO_INSUFICIENTE, pi.producto(), metodoPago);

        // 1) se retira una unidad (operacion interna, nunca expuesta por HTTP)
        if (inventario.despachar(codigo) != ResultadoEntrega.PRODUCTO_PREPARADO)
            return rechazo(EstadoCompra.SIN_EXISTENCIAS, pi.producto(), metodoPago);

        // 2) se registra el cobro en el monedero elegido
        ResultadoCobro cobro = monederos.get(metodoPago).cobrar(pi.precio(), cantidadRecibida);
        switch (cobro.estado()) {
            case COBRO_REALIZADO:
                return new ResultadoCompra(EstadoCompra.VENTA_REALIZADA, pi.producto(), metodoPago, cobro.cambio());
            case SIN_FONDOS_SUFICIENTES:
                inventario.reabastecer(codigo, 1);   // se regresa la unidad: no queda estado parcial
                return rechazo(EstadoCompra.SIN_FONDOS_SUFICIENTES, pi.producto(), metodoPago);
            default:
                inventario.reabastecer(codigo, 1);
                return rechazo(EstadoCompra.PAGO_INVALIDO, pi.producto(), metodoPago);
        }
    }

    public synchronized FondosMaquina consultarFondos() {
        double efectivo = redondear(monederos.get(MetodoPago.EFECTIVO).consultarFondos());
        double tarjeta = redondear(monederos.get(MetodoPago.TARJETA).consultarFondos());
        return new FondosMaquina(efectivo, tarjeta, redondear(efectivo + tarjeta));
    }

    private ResultadoCompra rechazo(EstadoCompra estado, Producto producto, MetodoPago metodoPago) {
        return new ResultadoCompra(estado, producto, metodoPago, 0.0);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
