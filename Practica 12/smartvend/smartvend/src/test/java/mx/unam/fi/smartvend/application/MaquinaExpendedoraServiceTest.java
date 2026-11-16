package mx.unam.fi.smartvend.application;

import mx.unam.fi.smartvend.application.service.MaquinaExpendedoraService;
import mx.unam.fi.smartvend.domain.compra.EstadoCompra;
import mx.unam.fi.smartvend.domain.compra.ResultadoCompra;
import mx.unam.fi.smartvend.domain.inventario.ResultadoReabastecimiento;
import mx.unam.fi.smartvend.domain.inventario.ResultadoRegistro;
import mx.unam.fi.smartvend.domain.pago.FondosMaquina;
import mx.unam.fi.smartvend.domain.pago.MetodoPago;
import mx.unam.fi.smartvend.domain.producto.Bebida;
import mx.unam.fi.smartvend.infrastructure.inventario.InventarioEnMemoria;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoEfectivo;
import mx.unam.fi.smartvend.infrastructure.pago.MonederoTarjeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaquinaExpendedoraServiceTest {

    private MaquinaExpendedoraService servicio;

    @BeforeEach
    void preparar() {
        servicio = new MaquinaExpendedoraService(
                new InventarioEnMemoria(), new MonederoEfectivo(500.00), new MonederoTarjeta(250.00));
        servicio.registrarProducto(new Bebida("B001", "Agua", LocalDate.of(2027, 5, 20)), 18.50);
        servicio.reabastecer("B001", 2);
    }

    private int existencias() {
        return servicio.buscarProducto("B001").orElseThrow().existencias();
    }

    @Test
    void registrarCodigoDuplicadoSeRechaza() {
        ResultadoRegistro r = servicio.registrarProducto(new Bebida("B001", "Otra", LocalDate.of(2027, 1, 1)), 10);
        assertEquals(ResultadoRegistro.CODIGO_DUPLICADO, r);
    }

    @Test
    void reabastecerConCantidadInvalidaSeRechaza() {
        assertEquals(ResultadoReabastecimiento.CANTIDAD_INVALIDA, servicio.reabastecer("B001", 0));
    }

    @Test
    void compraConEfectivoDevuelveCambio() {
        ResultadoCompra r = servicio.comprar("B001", MetodoPago.EFECTIVO, 20.00);
        assertEquals(EstadoCompra.VENTA_REALIZADA, r.estado());
        assertEquals(1.50, r.cambio(), 0.001);
        assertEquals(1, existencias());
        assertEquals(518.50, servicio.consultarFondos().efectivo(), 0.001);
    }

    @Test
    void compraConTarjetaNoGeneraCambioYUsaOtroMonedero() {
        ResultadoCompra r = servicio.comprar("B001", MetodoPago.TARJETA, 18.50);
        assertEquals(EstadoCompra.VENTA_REALIZADA, r.estado());
        assertEquals(0.0, r.cambio(), 0.001);
        FondosMaquina fondos = servicio.consultarFondos();
        assertEquals(500.00, fondos.efectivo(), 0.001);
        assertEquals(268.50, fondos.tarjeta(), 0.001);
        assertEquals(768.50, fondos.total(), 0.001);
    }

    @Test
    void tarjetaConMontoDistintoSeRechazaYNoCambiaNada() {
        ResultadoCompra r = servicio.comprar("B001", MetodoPago.TARJETA, 20.00);
        assertEquals(EstadoCompra.PAGO_INVALIDO, r.estado());
        assertEquals(2, existencias());
        assertEquals(250.00, servicio.consultarFondos().tarjeta(), 0.001);
    }

    @Test
    void pagoInsuficienteNoCambiaNada() {
        ResultadoCompra r = servicio.comprar("B001", MetodoPago.EFECTIVO, 10.00);
        assertEquals(EstadoCompra.PAGO_INSUFICIENTE, r.estado());
        assertEquals(2, existencias());
    }

    @Test
    void sinFondosParaElCambioRegresaLaUnidad() {
        MaquinaExpendedoraService pobre = new MaquinaExpendedoraService(
                new InventarioEnMemoria(), new MonederoEfectivo(0), new MonederoTarjeta(0));
        pobre.registrarProducto(new Bebida("B001", "Agua", LocalDate.of(2027, 5, 20)), 18.50);
        pobre.reabastecer("B001", 1);

        ResultadoCompra r = pobre.comprar("B001", MetodoPago.EFECTIVO, 20.00);

        assertEquals(EstadoCompra.SIN_FONDOS_SUFICIENTES, r.estado());
        assertEquals(1, pobre.buscarProducto("B001").orElseThrow().existencias());
        assertEquals(0.0, pobre.consultarFondos().efectivo(), 0.001);
    }

    @Test
    void sinExistenciasYProductoInexistente() {
        servicio.comprar("B001", MetodoPago.EFECTIVO, 18.50);
        servicio.comprar("B001", MetodoPago.EFECTIVO, 18.50);
        assertEquals(EstadoCompra.SIN_EXISTENCIAS, servicio.comprar("B001", MetodoPago.EFECTIVO, 18.50).estado());
        assertEquals(EstadoCompra.PRODUCTO_NO_ENCONTRADO, servicio.comprar("X", MetodoPago.EFECTIVO, 18.50).estado());
    }
}
